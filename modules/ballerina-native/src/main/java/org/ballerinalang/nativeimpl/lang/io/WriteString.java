package org.ballerinalang.nativeimpl.lang.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BFile;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Write String Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "writeString",
        args = {@Argument(name = "content", type = TypeEnum.STRING),
                @Argument(name = "file", type = TypeEnum.FILE),
                @Argument(name = "append", type = TypeEnum.BOOLEAN)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function writes a file using the given input stream") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "string",
        value = "String to be written") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "file",
        value = "Path of the file") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "append",
        value = "Append the content to the file") })
public class WriteString extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(WriteString.class);
    @Override public BValue[] execute(Context context) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        BFile target = (BFile) getArgument(context, 1);
        BString content = (BString) getArgument(context, 0);
        BBoolean append = (BBoolean) getArgument(context, 2);
        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            inputStream = new ByteArrayInputStream(content.stringValue().getBytes("UTF-8"));
            outputStream = targetObj.getContent().getOutputStream(append.booleanValue());
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();

        } catch (FileSystemException e) {
            throw new BallerinaException("Error while resolving file", e);
        } catch (IOException e) {
            throw new BallerinaException("Error while writing file", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return VOID_RETURN;
    }
}
