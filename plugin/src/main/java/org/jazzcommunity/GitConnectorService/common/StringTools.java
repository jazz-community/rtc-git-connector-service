package org.jazzcommunity.GitConnectorService.common;

public final class StringTools {
  public static String truncate(String input, int length) {
    return input.substring(0, Math.min(input.length(), length));
  }
}
