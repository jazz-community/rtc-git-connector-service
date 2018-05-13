package org.jazzcommunity.GitConnectorService.data;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import org.jazzcommunity.GitConnectorService.buildsecret.security.Crypto;
import org.jazzcommunity.GitConnectorService.buildsecret.internal.BuildSecretsHelper;
import org.jazzcommunity.GitConnectorService.buildsecret.internal.BuildSecretsReader;
import org.jazzcommunity.GitConnectorService.buildsecret.jazz.AdvancedProperties;
import org.jazzcommunity.GitConnectorService.buildsecret.jazz.User;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.GeneralSecurityException;

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
