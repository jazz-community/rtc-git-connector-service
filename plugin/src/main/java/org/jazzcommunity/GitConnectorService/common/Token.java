package org.jazzcommunity.GitConnectorService.common;

import com.ibm.team.repository.service.internal.ServerConfiguration;

public final class Token {
  private Token() {}

  public static String get() {
    @SuppressWarnings("restriction")
    ServerConfiguration config = ServerConfiguration.INSTANCE;
    @SuppressWarnings("restriction")
    String token =
        config.getStringConfigProperty("org.jazzcommunity.GitConnectorService", "access_token");
    return token;
  }
}
