package org.jazzcommunity.GitConnectorService.buildsecret.internal;

import com.ibm.team.build.internal.common.ITeamBuildService;
import com.ibm.team.build.internal.common.model.IBuildSecrets;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IManagedItem;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.service.IRepositoryItemService;
import com.ibm.team.repository.service.TeamRawService;

public final class BuildSecretsWriter {
    public static void storeSecret(TeamRawService trs, IContributor user, String key, String value) throws TeamRepositoryException {
        ITeamBuildService itbs = trs.getService(ITeamBuildService.class);
        IRepositoryItemService repoItemService = itbs.getService(IRepositoryItemService.class);

        IBuildSecrets secretStore = BuildSecretsHelper.queryBuildSecretsForContributor(trs, user);

        Object ssWorkingCopy;
        if (secretStore == null) {
            ssWorkingCopy = IBuildSecrets.ITEM_TYPE.createItem();
            ((IBuildSecrets) ssWorkingCopy).setOwner(user);
        } else {
            ssWorkingCopy = secretStore.getWorkingCopy();
        }

        ((IBuildSecrets) ssWorkingCopy).setProperty(key, value);

        IManagedItem managedHolder = (IManagedItem) ssWorkingCopy;
        managedHolder.setContextId(user.getContextId());

        repoItemService.saveItem(managedHolder);
    }
}
