package org.ballerinalang.model.types;/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.ballerinalang.model.SymbolScope;
import org.ballerinalang.model.values.BValue;

/**
 * {@code BReader} represents a Buffered Reader.
 *
 * @since 0.9.0
 */
public class BReaderType extends BType {

    protected BReaderType(SymbolScope symbolScope) {
        super(symbolScope);
    }

    protected BReaderType(String typeName, String pkgPath, SymbolScope symbolScope,
                          Class<? extends BValue> valueClass) {
        super(typeName, pkgPath, symbolScope, valueClass);
    }

    @Override public <V extends BValue> V getZeroValue() {
        return null;
    }

    @Override public <V extends BValue> V getEmptyValue() {
        return null;
    }
}