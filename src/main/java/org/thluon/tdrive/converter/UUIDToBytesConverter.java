package org.thluon.tdrive.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

@WritingConverter
public class UUIDToBytesConverter implements Converter<UUID, byte[]> {
    @Override
    public byte[] convert(final UUID source) {
        System.out.print("\n\n\n\nPARSING\n\n\n");
        final var bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(source.getMostSignificantBits());
        bb.putLong(source.getLeastSignificantBits());
        return bb.array();
    }
}
