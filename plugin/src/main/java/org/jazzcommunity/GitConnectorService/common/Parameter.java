package org.jazzcommunity.GitConnectorService.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;

public class Parameter {
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
}
