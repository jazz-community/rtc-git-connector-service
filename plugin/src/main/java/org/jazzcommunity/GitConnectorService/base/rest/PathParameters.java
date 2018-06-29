package org.jazzcommunity.GitConnectorService.base.rest;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParameters {
    public PathParameters(String path, String url) {
        // first, I want the names...
        // This I can probably do just once..., maybe when
        // creating the service. But I'm not going to optimize
        // this until later.
        ArrayList<String> names = getNames(path);

        System.out.println(String.format("names: %s", Joiner.on(',').join(names)));

        ArrayList<String> values = getValues(path, url);

        System.out.println(String.format("values: %s", Joiner.on(',').join(values)));

        if (names.size() != values.size()) {
            throw new RuntimeException("Unable to match url parameters to values");
        }

        Map<String, String> parameters = makeMap(names, values);
        for (String s : parameters.keySet()) {
            System.out.println(String.format("key: %s value: %s", s, parameters.get(s)));
        }
    }

    private Map<String, String> makeMap(ArrayList<String> names, ArrayList<String> values) {
        // zip operation
        HashMap<String, String> parameters = new HashMap<>();

        for (int i = 0; i < names.size(); i += 1) {
            parameters.put(names.get(i), values.get(i));
        }

        return parameters;
    }

    private static ArrayList<String> getValues(String path, String url) {
        // this is what I need to do, to actually get the values
        String regex = path.replaceAll("\\{[^\\/]+\\}", "([^\\\\/]+)");
        System.out.println(String.format("regex: %s", regex));
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
