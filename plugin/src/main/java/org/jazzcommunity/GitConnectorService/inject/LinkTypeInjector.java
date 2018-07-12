package org.jazzcommunity.GitConnectorService.inject;

import com.ibm.team.workitem.common.model.WorkItemLinkTypes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class injects our custom git link types into the {@link com.ibm.team.workitem.common.model.WorkItemLinkTypes}
 * static class. The reason for doing this is wanting to duplicate the behavior of the built-in
 * gitCommit link type. After following a <a href="https://rsjazz.wordpress.com/2014/12/03/creating-custom-link-types-for-rational-team-concert/">tutorial</a>
 * and trying to duplicate existing link types by looking at plugin.xml files in the RTC SDK, we
 * couldn't exactly achieve our desired behavior.
 *
 * <p>What we wanted:</p>
 * <ul>
 * <li> Show custom links on a work item </li>
 * <li> Do not show the link type in the drop down by default </li>
 * <li> Have our services and plugins all create these links </li>
 * <li> Let all users delete links of this type </li>
 * </ul>
 *
 * <p>
 * The gitCommit link type allows all of these use cases, but we couldn't get our link to
 * exactly match these requirements. With the configuration in the plugin.xml of this project,
 * we could get the first three behaviors, but the user was unable to delete this link type. No
 * other links we could find exhibit exactly the behavior of the gitCommit link.
 * </p>
 * <p>
 * After some digging, we found out that {@link com.ibm.team.workitem.common.model.WorkItemLinkTypes}
 * introduces custom, <em>hard-coded</em>, behavior for certain link types. Especially gitCommit
 * links are handled separately, by making them deletable by users, disregarding the rest of their
 * configuration.
 * </p>
 * <p>
 * To work around this, we decided to inject our own link types into the static collection, to
 * enable the same behavior as built-in git related links. This only affects client-side (work-item
 * editor) behavior, as it works around the {@code DynamicReadOnly} css class being set on links,
 * which is the only difference achieved by modifying the {@code USER_DELETABLE} collection.
 * </p>
 * <p>
 * All other behavior, namely for APIs, Plugins etc., stays exactly the same.
 * </p>
 *
 * <p>
 * The injection logic <em>MUST</em> be called in the service constructor, in order to properly
 * match the application life cycle.
 * </p>
 *
 * @see com.ibm.team.workitem.common.model.WorkItemLinkTypes
 * @see <a href="https://rsjazz.wordpress.com/2014/12/03/creating-custom-link-types-for-rational-team-concert/">Custom Link blog post</a>
 */
public class LinkTypeInjector {
    private LinkTypeInjector() {
    }

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
