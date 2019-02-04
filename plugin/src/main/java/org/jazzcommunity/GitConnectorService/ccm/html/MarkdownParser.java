package org.jazzcommunity.GitConnectorService.ccm.html;

import java.util.Arrays;
import java.util.List;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownParser {
  public static String toHtml(String markdown) {
    if (markdown == null) {
      return "";
    }

    List<Extension> extensions = Arrays.asList(TablesExtension.create());
    Parser parser = Parser.builder().extensions(extensions).build();
    Node document = parser.parse(markdown);
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    return renderer.render(document);
  }
}
