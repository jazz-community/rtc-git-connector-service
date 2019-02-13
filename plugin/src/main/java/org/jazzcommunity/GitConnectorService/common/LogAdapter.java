package org.jazzcommunity.GitConnectorService.common;

import com.google.common.base.Joiner;
import java.util.Collections;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLinkFactory;

public class LogAdapter {
  public static void header(Log log, HttpServletRequest request) {
    for (String key : Collections.list(request.getHeaderNames())) {
      log.debug(String.format("key: %s, value: %s", key, request.getHeader(key)));
    }
  }

  public static void remote(Log log, HttpServletRequest request) {
    log.debug(String.format("Remote User: %s", request.getRemoteUser()));
    log.debug(String.format("Request Uri: %s", request.getRequestURI()));
  }

  public static void parameters(Log log, HttpServletRequest request) {
    for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
      String value = Joiner.on(", ").join(entry.getValue());
      String out = String.format("Parameter %s: %s", entry.getKey(), value);
      log.debug(out);
    }
  }

  public static void link(Log log, WorkItemLinkFactory link) {
    log.debug(String.format("Item id: %s, uuid: %s", link.getId(), link.getItemId()));
  }
}
