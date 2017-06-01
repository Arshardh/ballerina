/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.runtime;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BLangVM;
import org.ballerinalang.bre.bvm.ControlStackNew;
import org.ballerinalang.model.values.BMessage;
import org.ballerinalang.model.values.BRefType;
import org.ballerinalang.natives.connectors.BallerinaConnectorManager;
import org.ballerinalang.services.DefaultServerConnectorErrorHandler;
import org.ballerinalang.services.ErrorHandlerUtils;
import org.ballerinalang.services.dispatchers.DispatcherRegistry;
import org.ballerinalang.services.dispatchers.ResourceDispatcher;
import org.ballerinalang.services.dispatchers.ServiceDispatcher;
import org.ballerinalang.util.codegen.CodeAttributeInfo;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.ResourceInfo;
import org.ballerinalang.util.codegen.ServiceInfo;
import org.ballerinalang.util.codegen.WorkerInfo;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.ServerConnectorErrorHandler;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * {@code ServerConnectorMessageHandler} is responsible for bridging Ballerina Program and External Server Connector.
 *
 * @since 0.8.0
 */
public class ServerConnectorMessageHandler {

    private static final Logger breLog = LoggerFactory.getLogger(ServerConnectorMessageHandler.class);

    private static PrintStream outStream = System.err;

    public static void handleInbound(CarbonMessage cMsg, CarbonCallback callback) {
        // Create the Ballerina Context
//        Context balContext = new Context(cMsg);
//        balContext.setServerConnectorProtocol(cMsg.getProperty("PROTOCOL"));
        try {
            String protocol = (String) cMsg.getProperty(org.wso2.carbon.messaging.Constants.PROTOCOL);
            if (protocol == null) {
                throw new BallerinaException("protocol not defined in the incoming request");
            }

            // Find the Service Dispatcher
            ServiceDispatcher dispatcher = DispatcherRegistry.getInstance().getServiceDispatcher(protocol);
            if (dispatcher == null) {
                throw new BallerinaException("no service dispatcher available to handle protocol: " + protocol);
            }

            // Find the Service
            ServiceInfo service = dispatcher.findService(cMsg, callback);
            if (service == null) {
                throw new BallerinaException("no Service found to handle the service request");
                // Finer details of the errors are thrown from the dispatcher itself, Ideally we shouldn't get here.
            }

            // Find the Resource Dispatcher
            ResourceDispatcher resourceDispatcher = DispatcherRegistry.getInstance().getResourceDispatcher(protocol);
            if (resourceDispatcher == null) {
                throw new BallerinaException("no resource dispatcher available to handle protocol: " + protocol);
            }

            // Find the Resource
            ResourceInfo resource = null;
            try {
                resource = resourceDispatcher.findResource(service, cMsg, callback);
            } catch (BallerinaException ex) {
                throw new BallerinaException("no resource found to handle the request to Service: " +
                        service.getServiceName() + " : " + ex.getMessage());
            }
            if (resource == null) {
                throw new BallerinaException("no resource found to handle the request to Service: " +
                        service.getServiceName());
                // Finer details of the errors are thrown from the dispatcher itself, Ideally we shouldn't get here.
            }

            // Delegate the execution to the BalProgram Executor
//            BalProgramExecutor.execute(cMsg, callback, resource, service, balContext);
            invokeResource(cMsg, callback, resource, service);

        } catch (Throwable throwable) {
            handleErrorInboundPath(cMsg, callback, throwable);
        }
    }

    /**
     * Resource invocation logic.
     *
     * @param carbonMessage  incoming carbonMessage
     * @param carbonCallback carbonCallback
     * @param resourceInfo   resource that has been invoked
     * @param serviceInfo    service that has been invoked
     */
    public static void invokeResource(CarbonMessage carbonMessage, CarbonCallback carbonCallback,
                                      ResourceInfo resourceInfo, ServiceInfo serviceInfo) {
        PackageInfo packageInfo = serviceInfo.getPackageInfo();

        // TODO : This is not supported yet. Fix this.
        if (resourceInfo.getParamTypes().length > 1) {
            throw new RuntimeException("Resource with Param not supported yet. ");
        }

        Context context = new Context();
        ControlStackNew controlStackNew = context.getControlStackNew();
        context.setBalCallback(new DefaultBalCallback(carbonCallback));

        // Now create callee's stackframe
        WorkerInfo defaultWorkerInfo = resourceInfo.getDefaultWorkerInfo();
        org.ballerinalang.bre.bvm.StackFrame calleeSF =
                new org.ballerinalang.bre.bvm.StackFrame(resourceInfo, defaultWorkerInfo, -1, new int[0]);
        controlStackNew.pushFrame(calleeSF);

        int longParamCount = 0;
        int doubleParamCount = 0;
        int stringParamCount = 0;
        int intParamCount = 0;
        int refParamCount = 0;

        CodeAttributeInfo codeAttribInfo = defaultWorkerInfo.getCodeAttributeInfo();

        long[] longLocalVars = new long[codeAttribInfo.getMaxLongLocalVars()];
        double[] doubleLocalVars = new double[codeAttribInfo.getMaxDoubleLocalVars()];
        String[] stringLocalVars = new String[codeAttribInfo.getMaxStringLocalVars()];
        // Setting the zero values for strings
        Arrays.fill(stringLocalVars, "");

        int[] intLocalVars = new int[codeAttribInfo.getMaxIntLocalVars()];
        BRefType[] refLocalVars = new BRefType[codeAttribInfo.getMaxRefLocalVars()];
        refLocalVars[0] = new BMessage(carbonMessage);

        // TODO : handle other resource parameters.

        calleeSF.setLongLocalVars(longLocalVars);
        calleeSF.setDoubleLocalVars(doubleLocalVars);
        calleeSF.setStringLocalVars(stringLocalVars);
        calleeSF.setIntLocalVars(intLocalVars);
        calleeSF.setRefLocalVars(refLocalVars);

        BLangVM bLangVM = new BLangVM(packageInfo.getProgramFile());
        bLangVM.execFunction(packageInfo, context, codeAttribInfo.getCodeAddrs());
    }

    public static void handleOutbound(CarbonMessage cMsg, CarbonCallback callback) {
//        BalConnectorCallback connectorCallback = (BalConnectorCallback) callback;
//        try {
        callback.done(cMsg);
//            if (connectorCallback.isNonBlockingExecutor()) {
//                // Continue Non-Blocking
//                BLangExecutionVisitor executor = connectorCallback.getContext().getExecutor();
//                executor.continueExecution(connectorCallback.getCurrentNode().next());
//            }
//        } catch (Throwable throwable) {
//            handleErrorFromOutbound(cMsg, connectorCallback.getContext(), throwable);
//        }
    }

    public static void handleErrorInboundPath(CarbonMessage cMsg, CarbonCallback callback,
                                              Throwable throwable) {
        String errorMsg = ErrorHandlerUtils.getErrorMessage(throwable);
        String stacktrace = ""; //ErrorHandlerUtils.getServiceStackTrace(balContext, throwable);
        String errorWithTrace = errorMsg + "\n" + stacktrace;
        outStream.println(errorWithTrace);

        // bre log should contain bre stack trace, not the ballerina stack trace
        breLog.error("error: " + errorMsg + ", ballerina service stack trace: " + stacktrace, throwable);
        Object protocol = cMsg.getProperty("PROTOCOL");
        Optional<ServerConnectorErrorHandler> optionalErrorHandler =
                BallerinaConnectorManager.getInstance().getServerConnectorErrorHandler((String) protocol);

        try {
            optionalErrorHandler
                    .orElseGet(DefaultServerConnectorErrorHandler::getInstance)
                    .handleError(new BallerinaException(errorMsg, throwable.getCause()), cMsg, callback);
        } catch (Exception e) {
            throw new BallerinaException("Cannot handle error using the error handler for: " + protocol, e);
        }

    }

    public static void handleErrorFromOutbound(Context balContext, Throwable throwable) {
        String errorMsg = ErrorHandlerUtils.getErrorMessage(throwable);
        String stacktrace = ErrorHandlerUtils.getServiceStackTrace(balContext, throwable);
        String errorWithTrace = errorMsg + "\n" + stacktrace;
        outStream.println(errorWithTrace);

        // bre log should contain bre stack trace, not the ballerina stack trace
        breLog.error("error: " + errorMsg + ", ballerina service stack trace: " + stacktrace, throwable);

        Object protocol = balContext.getServerConnectorProtocol();
        Optional<ServerConnectorErrorHandler> optionalErrorHandler =
                BallerinaConnectorManager.getInstance().getServerConnectorErrorHandler((String) protocol);
        try {
            optionalErrorHandler
                    .orElseGet(DefaultServerConnectorErrorHandler::getInstance)
                    .handleError(new BallerinaException(errorMsg, throwable.getCause(), balContext), null,
                            balContext.getBalCallback());
        } catch (Exception e) {
            throw new BallerinaException("Cannot handle error using the error handler for: " + protocol, e);
        }
    }

}
