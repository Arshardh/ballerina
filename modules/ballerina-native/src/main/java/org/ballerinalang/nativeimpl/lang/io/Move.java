package org.ballerinalang.nativeimpl.lang.io;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BFile;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "move",
        args = {@Argument(name = "target", type = TypeEnum.FILE),
                @Argument(name = "destination", type = TypeEnum.FILE)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function moves a file from a given location to another") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "target",
        value = "File/Directory that should be copied") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "destination",
        value = "The location where the File/Directory should be pasted") })
public class Move extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(Move.class);
    @Override public BValue[] execute(Context context) {
        BFile target = (BFile) getArgument(context, 0);
        BFile destination = (BFile) getArgument(context, 1);

        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            if (targetObj.exists()) {
                FileObject destinationObj = fsManager.resolveFile(destination.stringValue());
                targetObj.moveTo(destinationObj);
            }

        } catch (FileSystemException e) {
            throw new BallerinaException("Error while resolving file", e);
        }
        return VOID_RETURN;
    }
}
