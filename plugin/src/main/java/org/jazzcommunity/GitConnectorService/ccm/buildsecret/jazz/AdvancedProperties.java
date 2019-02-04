package org.jazzcommunity.GitConnectorService.ccm.buildsecret.jazz;

import com.ibm.team.repository.service.internal.ServerConfiguration;

public final class AdvancedProperties {
  public static String getPrivateKey() {
    @SuppressWarnings("restriction")
    ServerConfiguration sc = ServerConfiguration.INSTANCE;
    @SuppressWarnings("restriction")
    String aa =
        sc.getStringConfigProperty(
            "com.siemens.bt.jazz.services.PersonalTokenService", "privateKey");
    return aa;
  }
}
