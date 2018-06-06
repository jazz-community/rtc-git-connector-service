package org.jazzcommunity.GitConnectorService.oslc.mapping;

import com.google.common.base.Joiner;
import org.modelmapper.AbstractConverter;

import java.util.Collection;

public final class Converters {
    private Converters() {}

    public static AbstractConverter<Collection<String>, String> listToString() {
        return new AbstractConverter<Collection<String>, String>() {
            @Override
            protected String convert(Collection<String> strings) {
                return Joiner.on(", ").join(strings);
            }
        };
    }

    public static AbstractConverter<String, Boolean> state() {
        return new AbstractConverter<String, Boolean>() {
            @Override
            protected Boolean convert(String state) {
                return state != null;
            }
        };
    }
}
