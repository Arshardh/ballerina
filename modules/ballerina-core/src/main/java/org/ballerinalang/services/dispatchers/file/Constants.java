/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.services.dispatchers.file;

/**
 * Constants for File server connector.
 */
public class Constants {
    public static final String ANNOTATION_FILE_SOURCE = "FileSource";
    public static final String PROTOCOL_FILE = "file";
    public static final String TRANSPORT_PROPERTY_SERVICE_NAME = "TRANSPORT_FILE_SERVICE_NAME";
    public static final String FILE_PACKAGE_NAME = "ballerina.net.file";
    public static final String FILE_UPDATE = "FILE_UPDATE";
    public static final String FILE_ROTATE = "FILE_ROTATE";
    public static final String FILE_TRANSPORT_EVENT_NAME = "FILE_TRANSPORT_EVENT_NAME";
    public static final String ANNOTATION_NAME_ON_UPDATE = "OnUpdate";
    public static final String ANNOTATION_NAME_ON_ROTATE = "OnRotate";
    public static final String ANNOTATION_ATTRIBUTE_PROTOCOL = "protocol";
    public static final String ANNOTATION_ATTRIBUTE_URI = "fileURI";
    public static final String ANNOTATION_ATTRIBUTE_POLLING_INTERVAL = "pollingInterval";
    public static final String ANNOTATION_ATTRIBUTE_SEEK = "seek";
    public static final String ANNOTATION_ATTRIBUTE_MAX_LINES_PER_POLL = " maxLinesPerPoll";
}
