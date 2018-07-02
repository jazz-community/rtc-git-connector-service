package org.jazzcommunity.GitConnectorService.base.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParameters {
    private final Map<String, String> parameters;

    public PathParameters(String path, String url) {
        this.parameters = makeMap(path, url);
    }

    // getters should do checking if stuff exists etc.
    public String get(String key) {
        return parameters.get(key);
    }

    public Integer getAsInteger(String key) {
        return Integer.parseInt(get(key));
    }

    private static Map<String, String> makeMap(String path, String url) {
        HashMap<String, String> parameters = new HashMap<>();
        String regex = path.replaceAll(
                "\\{([^\\/}]+)\\}",
                "([^\\\\/]+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher names = pattern.matcher(path);
        Matcher values = pattern.matcher(url);

        if (names.groupCount() != values.groupCount()) {
            /*
             * This should never happen if routing is actually successful. If we reach this
             * exception, something is fundamentally wrong and we have overlooked a core
             * url mapping concept.
             */
            throw new RuntimeException("Unable to match url parameters to values");
        }

        while (names.find() && values.find()) {
            /*
             * Skip the leading group because it is always the entire match.
             * For a visualization how this regex works for creating the parameter map,
             * refer to the provided example: https://regex101.com/r/17Ul3V/1
             */
            for (int i = 1; i <= names.groupCount(); i += 1) {
                String name = names.group(i).substring(1, names.group(i).length() - 1);
                parameters.put(name, values.group(i));
            }
        }

        return parameters;
    }
}
