package org.jazzcommunity.GitConnectorService.ccm.buildsecret.internal;

import com.ibm.team.build.internal.common.ITeamBuildService;
import com.ibm.team.build.internal.common.model.IBuildSecrets;
import com.ibm.team.build.internal.common.model.query.BaseBuildSecretsQueryModel;
import com.ibm.team.build.internal.service.QueryHelper;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IItem;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.query.IItemQuery;
import com.ibm.team.repository.common.query.ast.IItemHandleInputArg;
import com.ibm.team.repository.service.IRepositoryItemService;
import com.ibm.team.repository.service.IServerQueryService;
import com.ibm.team.repository.service.TeamRawService;

public final class BuildSecretsHelper {
  static IBuildSecrets queryBuildSecretsForContributor(TeamRawService trs, IContributor user)
      throws TeamRepositoryException {
    ITeamBuildService itbs = trs.getService(ITeamBuildService.class);
    IServerQueryService serverQueryService = itbs.getService(IServerQueryService.class);
    IRepositoryItemService repoItemService = itbs.getService(IRepositoryItemService.class);
    BaseBuildSecretsQueryModel.BuildSecretsQueryModel queryModel =
        BaseBuildSecretsQueryModel.BuildSecretsQueryModel.ROOT;
    IItemQuery query = IItemQuery.FACTORY.newInstance(queryModel);
    IItemHandleInputArg prec = query.newItemHandleArg();
    query.filter(queryModel.owner()._eq(prec));
    IItem[] results =
        (new QueryHelper(serverQueryService, repoItemService))
            .queryItems(query, new Object[] {user}, IRepositoryItemService.COMPLETE, 1);
    return (IBuildSecrets) (results.length == 0 ? null : results[0]);
  }

  public static String generateKey(String user, String scope) {
    // Important: return value must have at least 16 digits
    return user + "-" + scope + "-TokenServiceSuffix";
  }
}
