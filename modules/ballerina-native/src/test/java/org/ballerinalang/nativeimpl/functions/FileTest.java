/*
*   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
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
package org.ballerinalang.nativeimpl.functions;

import org.ballerinalang.model.BLangProgram;
import org.ballerinalang.model.GlobalScope;
import org.ballerinalang.model.StructDef;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.util.BTestUtils;
import org.ballerinalang.util.program.BLangFunctions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Test cases for ballerina.model.arrays.
 */
public class FileTest {

    private BLangProgram bLangProgram;

    @BeforeClass
    public void setup() {
        bLangProgram = BTestUtils.parseBalFile("samples/fileTest.bal");
    }

    @BeforeMethod
    public void createTempDir() {
        File temp = new File("temp");
        if (temp.exists()) {
            deleteDir(temp);
        } else {
            temp.mkdir();
        }
    }

    @AfterMethod
    public void deleteTempDir() {
        File temp = new File("temp");
        if (temp.exists()) {
            deleteDir(temp);
        }
    }


    @Test
    public void testCopy() throws IOException {

        String sourcePath = "temp/original.txt";
        String destPath = "temp/duplicate.txt";
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
            if (sourceFile.createNewFile()) {

                BValue[] source = { new BString(sourcePath) };
                BValue[] dest = { new BString(destPath) };
                BStruct sourceStruct = new BStruct(new StructDef(GlobalScope.getInstance()), source);
                BStruct destStruct = new BStruct(new StructDef(GlobalScope.getInstance()), dest);
                BValue[] args = { sourceStruct, destStruct };

                BLangFunctions.invoke(bLangProgram, "testCopy", args);
                Assert.assertTrue(sourceFile.exists(), "Source file does not exist");
                Assert.assertTrue(destFile.exists(), "File wasn't copied");
            } else {
                Assert.fail("Error in file creation.");
            }
        }

    @Test
    public void testMove() throws IOException {

        String sourcePath = "temp/original.txt";
        String destPath = "temp/test/original.txt";
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
            if (sourceFile.createNewFile()) {

                BValue[] source = { new BString(sourcePath) };
                BValue[] dest = { new BString(destPath) };
                BStruct sourceStruct = new BStruct(new StructDef(GlobalScope.getInstance()), source);
                BStruct destStruct = new BStruct(new StructDef(GlobalScope.getInstance()), dest);
                BValue[] args = { sourceStruct, destStruct };

                BLangFunctions.invoke(bLangProgram, "testMove", args);
                Assert.assertFalse(sourceFile.exists(), "Source file exists");
                Assert.assertTrue(destFile.exists(), "File wasn't moved");
            } else {
                Assert.fail("Error in file creation.");
            }
    }

    @Test
    public void testDelete() throws IOException {

        String targetPath = "temp/original.txt";
        File targetFile = new File(targetPath);
            if (targetFile.createNewFile()) {

                BValue[] source = { new BString(targetPath) };
                BStruct targetStruct = new BStruct(new StructDef(GlobalScope.getInstance()), source);
                BValue[] args = {targetStruct};

                BLangFunctions.invoke(bLangProgram, "testDelete", args);
                Assert.assertFalse(targetFile.exists(), "Target file exists");
            } else {
                Assert.fail("Error in file creation.");
            }
    }

    @Test
    public void testOpen() throws IOException {

        String sourcePath = "temp/original.txt";
        File sourceFile = new File(sourcePath);
            if (sourceFile.createNewFile()) {

                BValue[] source = { new BString(sourcePath) };
                BStruct sourceStruct = new BStruct(new StructDef(GlobalScope.getInstance()), source);
                BValue[] args = {sourceStruct};

                BLangFunctions.invoke(bLangProgram, "testOpen", args);
                Assert.assertNotNull(sourceStruct.getNativeData("stream"), "Input Stream not found");
            } else {
                Assert.fail("Error in file creation.");
            }

    }

    private void deleteDir(File dir) {

        String[] entries = dir.list();
        if (entries.length != 0) {
            for (String s : entries) {
                File currentFile = new File(dir.getPath(), s);
                if (currentFile.isDirectory()) {
                    deleteDir(currentFile);
                }
                currentFile.delete();
            }
        }
        dir.delete();
    }


}
