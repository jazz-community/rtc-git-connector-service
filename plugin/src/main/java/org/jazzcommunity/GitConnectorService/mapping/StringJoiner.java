package org.jazzcommunity.GitConnectorService.mapping;

import java.util.Collection;

/**
 * This could be solved using Google's guava library. To reduce dependencies, this class was
 * added. However, if we need more functionality of this kind, we really should consider
 * using a proper library for this.
 */
public class StringJoiner {
    private StringJoiner() {}

    public static String join(Collection<String> strings) {
        return join(strings, "");
    }

    public static String join(Collection<String> strings, String on) {
        if (strings == null || strings.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
            builder.append(on);
        }
//        builder.delete(builder.length() - on.length() - 1, builder.length() - 1);
        return builder.toString();
    }
}
