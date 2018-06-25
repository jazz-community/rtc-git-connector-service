package org.jazzcommunity.GitConnectorService.oslc.type;

import org.modelmapper.ModelMapper;

public final class PrefixBuilder {

    private PrefixBuilder() {
    }

    public static <T> T get(Class<T> out) {
        // This would also be a place where a reflective type check might be
        // really nice to have. Check if we're actually mapping something that
        // is assignable.
        // Maybe I can just use Typeconverter here anyway...
        return new ModelMapper().map(new PrefixPrototype(), out);
    }
}
