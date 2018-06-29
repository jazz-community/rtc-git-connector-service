package org.jazzcommunity.GitConnectorService.base.rest;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParameters {
    public PathParameters(String path, String url) {
        // first, I want the names...
        // This I can probably do just once..., maybe when
        // creating the service. But I'm not going to optimize
        // this until later.
        String regex = "\\{([^\\/]+)\\}";
        System.out.println(regex);
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);

        ArrayList<String> names = new ArrayList<>();
        while(matcher.find()) {
            names.add(matcher.group(0));
        }

        String join = Joiner.on(',').join(names);
        System.out.println(join);


        // this is what I need to do, to actually get the values
//        String regex = path.replaceAll("\\{", "(");
//        regex = regex.replaceAll("\\}", ")");
//        Pattern pattern = Pattern.compile(regex);
    }
}
