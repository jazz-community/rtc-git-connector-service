package org.jazzcommunity.GitConnectorService.ccm.data;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import org.jazzcommunity.GitConnectorService.ccm.buildsecret.internal.BuildSecretsHelper;
import org.jazzcommunity.GitConnectorService.ccm.buildsecret.internal.BuildSecretsReader;
import org.jazzcommunity.GitConnectorService.ccm.buildsecret.jazz.AdvancedProperties;
import org.jazzcommunity.GitConnectorService.ccm.buildsecret.jazz.User;
import org.jazzcommunity.GitConnectorService.ccm.buildsecret.security.Crypto;

public class TokenHelper {
  public static String getToken(URL url, TeamRawService parentService) {
    IContributor user = User.getCurrentContributor(parentService);

    if (user == null) {
      return "";
    }

    try {
      String hash = BuildSecretsHelper.generateKey(user.getUserId(), url.getHost());
      String key = AdvancedProperties.getPrivateKey();
      String secret = BuildSecretsReader.getSecretByContributor(parentService, user, hash);
      String token = Crypto.decrypt(secret, key);
      return token;
    } catch (TeamRepositoryException | UnsupportedEncodingException | GeneralSecurityException e) {
      return "";
    }
  }
}
