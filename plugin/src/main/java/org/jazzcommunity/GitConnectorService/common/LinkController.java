package org.jazzcommunity.GitConnectorService.common;

import com.ibm.team.repository.service.TeamRawService;

public class LinkController {
  private final GitLink[] linkTypes;
  private final TeamRawService teamService;

  public LinkController(GitLink linkType, TeamRawService teamService) {
    this(new GitLink[] {linkType}, teamService);
  }

  public LinkController(GitLink[] linkTypes, TeamRawService teamService) {
    this.linkTypes = linkTypes;
    this.teamService = teamService;
  }

  public void collect(boolean includeArchived) {
  }
}
