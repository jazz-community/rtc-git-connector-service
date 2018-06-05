package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Links;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.GitCmLinks;

public class LinksMapper {
    public static GitCmLinks map(Links from) {
        GitCmLinks to = new GitCmLinks();
        return to;
    }
}
