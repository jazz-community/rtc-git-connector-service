package org.jazzcommunity.GitConnectorService.inject;

import com.ibm.team.workitem.common.model.WorkItemLinkTypes;

import java.lang.reflect.Field;
import java.util.HashSet;

public class LinkTypeInjector {
    public static void setDeletable() throws NoSuchFieldException, IllegalAccessException {
        Field deletable = WorkItemLinkTypes.class.getDeclaredField("USER_DELETABLE");
        deletable.setAccessible(true);
        HashSet<String> set = (HashSet<String>) deletable.get(new WorkItemLinkTypes());
        set.add("org.jazzcommunity.git.link.git_issue");
        set.add("org.jazzcommunity.git.link.git_mergerequest");
    }
}
