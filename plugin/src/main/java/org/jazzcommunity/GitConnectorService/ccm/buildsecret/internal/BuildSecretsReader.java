package org.jazzcommunity.GitConnectorService.ccm.buildsecret.internal;

import com.ibm.team.build.common.model.IBuildProperty;
import com.ibm.team.build.internal.common.model.IBuildSecrets;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.TeamRawService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BuildSecretsReader {

  public static String getSecretByContributor(TeamRawService trs, IContributor user, String key)
      throws TeamRepositoryException {
    List props = getSecretsByContributor(trs, user);
    if (props != null && key != null) {
      Iterator<IBuildProperty> iter = props.iterator();
      while (iter.hasNext()) {
        IBuildProperty prop = iter.next();
        if (key.equals(prop.getName())) {
          return prop.getValue();
        }
      }
    }
    return "";
  }

  private static List getSecretsByContributor(TeamRawService trs, IContributor user)
      throws TeamRepositoryException {
    IBuildSecrets secretStore = BuildSecretsHelper.queryBuildSecretsForContributor(trs, user);
    if (secretStore == null) {
      return new ArrayList();
    }
    return secretStore.getProperties();
  }
}
