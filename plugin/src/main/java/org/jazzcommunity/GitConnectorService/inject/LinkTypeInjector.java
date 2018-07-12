package org.jazzcommunity.GitConnectorService.inject;

import com.ibm.team.workitem.common.model.WorkItemLinkTypes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class LinkTypeInjector {
    private static List<String> CUSTOM_GIT_LINKS = Arrays.asList(
            "org.jazzcommunity.git.link.git_issue",
            "org.jazzcommunity.git.link.git_mergerequest"
    );

    private static void setDeletable() throws NoSuchFieldException, IllegalAccessException {
        Field deletable = WorkItemLinkTypes.class.getDeclaredField("USER_DELETABLE");
        deletable.setAccessible(true);
        HashSet<String> set = (HashSet<String>) deletable.get(new WorkItemLinkTypes());
        set.addAll(CUSTOM_GIT_LINKS);
    }

    public static void injectCustomLinks() {
        Logger logger = Logger.getLogger("LinkTypeInjector");
        logger.info("Injecting custom link types");

        try {
            setDeletable();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.severe("Unable to inject valid link types");
        }
    }
}
