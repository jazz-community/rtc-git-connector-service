package org.jazzcommunity.GitConnectorService.ccm.buildsecret.jazz;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.IRepositoryItemService;
import com.ibm.team.repository.service.TeamRawService;

public final class User {
  public static IContributor getCurrentContributor(TeamRawService parentService) {
    IContributorHandle userHandle = parentService.getAuthenticatedContributor();
    IRepositoryItemService itemService = parentService.getService(IRepositoryItemService.class);
    try {
      return (IContributor) itemService.fetchItem(userHandle, null);
    } catch (TeamRepositoryException e) {
      return null;
    }
  }
}
