package org.jazzcommunity.GitConnectorService.base.rest;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParameters {
    private final Map<String, String> parameters;

    public PathParameters(String path, String url) {
        // first, I want the names...
        // This I can probably do just once..., maybe when
        // creating the service. But I'm not going to optimize
        // this until later.
        ArrayList<String> names = getNames(path);
        ArrayList<String> values = getValues(path, url);

        if (names.size() != values.size()) {
            throw new RuntimeException("Unable to match url parameters to values");
        }

        this.parameters = makeMap(names, values);
    }

    // getters should do checking if stuff exists etc.
    public String get(String key) {
        return parameters.get(key);
    }

    public Integer getAsInteger(String key) {
        return Integer.parseInt(get(key));
    }

    private Map<String, String> makeMap(ArrayList<String> names, ArrayList<String> values) {
        // zip operation
        HashMap<String, String> parameters = new HashMap<>();

        for (int i = 0; i < names.size(); i += 1) {
            parameters.put(names.get(i), values.get(i));
        }

        return parameters;
    }

    // It's probably possible to consolidate these two functions, maybe even into a single
    // regex call.
    // Idea: Match the all-match regex to both path and url, which should give the name
    // in one match and the value in the other. Then the zip could be done right away as well.
    private static ArrayList<String> getValues(String path, String url) {
        String regex = path.replaceAll("\\{[^\\/]+\\}", "([^\\\\/]+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        ArrayList<String> values = new ArrayList<>();
        matcher.find();

        for (int i = 1; i <= matcher.groupCount(); i += 1) {
            values.add(matcher.group(i));
        }

        return values;
    }

    private static ArrayList<String> getNames(String path) {
        String regex = "\\{([^\\/]+)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);

        ArrayList<String> names = new ArrayList<>();
        while (matcher.find()) {
            names.add(matcher.group(1));
        }

        return names;
    }
}
