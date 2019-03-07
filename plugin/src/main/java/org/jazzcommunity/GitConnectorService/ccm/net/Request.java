package org.jazzcommunity.GitConnectorService.ccm.net;

import com.google.common.net.MediaType;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.jazzcommunity.GitConnectorService.ccm.properties.PropertyReader;

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
}
