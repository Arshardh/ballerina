package org.ballerinalang.model.values;

import org.ballerinalang.model.types.BType;
import org.ballerinalang.model.types.BTypes;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * The {@code BFloat} represents a buffer of an input stream.
 * {@link BInputStream} will be useful for reading input streams.
 *
 * @since 0.9.0
 */
public class BInputStream extends BufferedInputStream implements BRefType {

    public BInputStream(InputStream in) {
        super(in);
    }

    public BInputStream(InputStream in, int size) {
        super(in, size);
    }

    @Override
    public String stringValue() {
        return null;
    }

    @Override
    public BType getType() {
        return BTypes.typeInputStream;
    }

    @Override
    public Object value() {
        return null;
    }
}
