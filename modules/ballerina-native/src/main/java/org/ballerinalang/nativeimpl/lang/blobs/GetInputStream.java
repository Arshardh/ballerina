/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 * <p>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.nativeimpl.lang.blobs;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BBlob;
import org.ballerinalang.model.values.BInputStream;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;

import java.io.ByteArrayInputStream;

/**
 * Get Inputstream from a blob
 */
@BallerinaFunction(
        packageName = "ballerina.lang.blobs",
        functionName = "getInputStream",
        args = {@Argument(name = "b", type = TypeEnum.BLOB)},
        returnType = {@ReturnType(type = TypeEnum.INPUTSTREAM)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = {@Attribute(name = "value",
        value = "Get an inputstream from a blob") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "b",
        value = "BLOB value to be converted") })
@BallerinaAnnotation(annotationName = "Return", attributes = {@Attribute(name = "InputStream",
        value = "InputStream representation of the given BLOB") })
public class GetInputStream extends AbstractNativeFunction {

    public BValue[] execute(Context ctx) {
        BBlob msg = (BBlob) getArgument(ctx, 0);
        byte[] arr = msg.value();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr);
        return getBValues(new BInputStream(byteArrayInputStream));
    }
}
