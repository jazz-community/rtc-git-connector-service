package org.jazzcommunity.GitConnectorService.oslc.mapping;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

public final class TypeConverter {
    private TypeConverter() {
    }

    // Also, maybe instead of passing a class, I want to Pass a type here...
    public static <From, To> AbstractConverter<From, To> to(final Class<To> to) {
        // It would be nice to have a reflective check here to see if the
        // two types are actually compatible... Or maybe find out how
        // to define inheritance with auto-generated types.
        return new AbstractConverter<From, To>() {
            @Override
            protected To convert(From from) {
                if (from == null) {
                    return null;
                }

                return new ModelMapper().map(from, to);
            }
        };
    }
}
