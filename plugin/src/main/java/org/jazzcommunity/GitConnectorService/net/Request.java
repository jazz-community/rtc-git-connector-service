package org.jazzcommunity.GitConnectorService.net;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

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
}
