package org.jazzcommunity.GitConnectorService.oslc.mapping;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;

public final class TypeConverter {
    private TypeConverter() {
    }

    public static <From, To> To convert(final From from, final Type type) {
        if (from == null) {
            return null;
        }

        return new ModelMapper().map(from, type);
    }

    public static <From, To> AbstractConverter<From, To> to(final Type type) {
        // It would be nice to have a reflective check here to see if the
        // two types are actually compatible... Or maybe find out how
        // to define inheritance with auto-generated types.
        return new AbstractConverter<From, To>() {
            @Override
            protected To convert(From from) {
                if (from == null) {
                    return null;
                }

                return new ModelMapper().map(from, type);
            }
        };
    }
}
