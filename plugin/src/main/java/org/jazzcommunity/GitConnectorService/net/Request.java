package org.jazzcommunity.GitConnectorService.net;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    public static boolean isLinkRequest(HttpServletRequest request) {
        return request.getHeader("Accept").contains("application/x-jazz-compact-rendering")
                || request.getHeader("Accept").contains("application/x-oslc-compact+xml");
    }

    public static boolean isOslcRequest(HttpServletRequest request) {
        return request.getHeader("Accept").contains("application/json");
    }

    // for debugging
    public static void printHeader(HttpServletRequest request) {
        for (String key : Collections.list(request.getHeaderNames())) {
            System.out.println(String.format("key: %s, value: %s", key, request.getHeader(key)));
        }
    }

    public static UrlParameters getParameters(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String regex = ".*/gitlab\\/([^\\/]+)\\/project\\/([^\\/]+)\\/[^\\/]+\\/([^\\/]+)\\/.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);
        matcher.find();
        String host = matcher.group(1);
        String project = matcher.group(2);
        String artifact = matcher.group(3);

        return new UrlParameters(host, project, artifact);
    }
}
