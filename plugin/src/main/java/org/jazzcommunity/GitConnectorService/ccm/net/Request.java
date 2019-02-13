package org.jazzcommunity.GitConnectorService.ccm.net;

import com.google.common.base.Joiner;
import com.google.common.net.MediaType;
import java.io.IOException;
import java.util.Collections;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.jazzcommunity.GitConnectorService.ccm.properties.PropertyReader;
import org.apache.commons.logging.Log;

public class Request {
  public static boolean isLinkRequest(HttpServletRequest request) throws IOException {
    PropertyReader properties = new PropertyReader();
    return request.getHeader("Accept").contains(properties.get("content.type.link.compact"))
        || request.getHeader("Accept").contains(properties.get("content.type.link.oslc"));
  }

  public static boolean isOslcRequest(HttpServletRequest request) {
    return MediaType.JSON_UTF_8.toString().contains(request.getHeader("Accept"));
  }

  public static boolean isUrlHoverRequest(HttpServletRequest request) {
    if (request.getParameter("_context") == null
        || request.getParameter("_selector") == null
        || request.getParameter("_mediaType") == null) {
      return false;
    }
    return request.getParameter("_context").contains("web")
        && request.getParameter("_selector").contains("Hover")
        && request.getParameter("_mediaType").contains("text/html");
  }

  // for debugging
  public static void logHeader(Log log, HttpServletRequest request) {
    for (String key : Collections.list(request.getHeaderNames())) {
      log.debug(String.format("key: %s, value: %s", key, request.getHeader(key)));
    }
  }

  public static void logRemote(Log log, HttpServletRequest request) {
    log.debug(String.format("Remote User: %s", request.getRemoteUser()));
    log.debug(String.format("Request Uri: %s", request.getRequestURI()));
  }

  public static void logParameters(Log log, HttpServletRequest request) {
    for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
      String value = Joiner.on(", ").join(entry.getValue());
      String out = String.format("Parameter %s: %s", entry.getKey(), value);
      log.debug(out);
    }
  }
}
