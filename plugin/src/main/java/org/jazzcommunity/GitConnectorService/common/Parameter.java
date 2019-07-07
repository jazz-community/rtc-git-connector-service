package org.jazzcommunity.GitConnectorService.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;

public class Parameter {
  public static boolean handleArchived(HttpServletRequest request) {
    return Boolean.valueOf(request.getParameter("archived"));
  }

  public static int handleTimeout(HttpServletRequest request, int unset) {
    String timeout = request.getParameter("timeout");

    if (timeout == null) {
      return unset;
    }

    try {
      return Integer.valueOf(timeout);
    } catch (NumberFormatException e) {
      return unset;
    }
  }

  public static String handleId(HttpServletRequest request) {
    String id = request.getParameter("id");

    if (id == null) {
      return RandomStringUtils.randomAlphanumeric(1 << 5);
    } else {
      return id;
    }
  }

  // url for testing:
  // https://localhost:7443/jazz/service/org.jazzcommunity.GitConnectorService.IGitConnectorService/dcc/issues?size=25&modifiedsince=2019-07-01T09%3A14%3A17.704-0500
  public static String handleModified(HttpServletRequest request) {
    return request.getParameter("modifiedsince");
  }
}
