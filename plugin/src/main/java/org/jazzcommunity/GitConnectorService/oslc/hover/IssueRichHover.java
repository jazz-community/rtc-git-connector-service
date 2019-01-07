package org.jazzcommunity.GitConnectorService.oslc.hover;

import ch.sbi.minigit.type.gitlab.issue.Issue;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import org.jazzcommunity.GitConnectorService.html.MarkdownParser;
import org.jazzcommunity.GitConnectorService.net.GitServiceArtifact;
import org.jazzcommunity.GitConnectorService.properties.PropertyReader;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class IssueRichHover {
  public static void render(Issue issue, OutputStream stream) throws IOException {
    String description = MarkdownParser.toHtml(issue.getDescription());

    PropertyReader properties = new PropertyReader();
    JtwigTemplate template =
        JtwigTemplate.classpathTemplate(properties.get("template.hover.issue"));
    JtwigModel model =
        JtwigModel.newModel()
            .with("title", issue.getTitle())
            .with("description", description)
            .with("author", issue.getAuthor().getName())
            .with("state", issue.getState());

    template.render(model, stream);
  }
}
