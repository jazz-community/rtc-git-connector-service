package org.jazzcommunity.GitConnectorService.dcc.xml;

public final class XmlSanitizer {
  /**
   * Removes illegal xml characters from a utf-16 string.
   *
   * <p>JAXB is known to generate illegal xml characters. This issue:
   * https://github.com/eclipse-ee4j/jaxb-ri/issues/614 covers the details of these encoding
   * problems, which are non-trivial at best.
   *
   * <p>I think removing these non-printable characters without further consideration is a sound
   * solution t our problem, as it is data that is unnecessary for reporting. No obviously valuable
   * data is lost in removing these sequences.
   *
   * <p>Fixing this here is necessary because of the way the IGitResourceRestService encodes link
   * data. It is possible to encode invalid byte sequences into the link payload that is used in the
   * value parameter to the service. Because the data collection data must be transferred using xml,
   * any invalid byte sequences will result in an invalid payload and a non-functioning data
   * collection job. An example for such a malformed payload is:
   *
   * <p>H4sIAG14YFoAA42OUUvDMBSFf4kQO5_UtsmaLm2fNrepL4Ox1bcipMltG0aSkaaIiP_dTBB8Et8uH98593xEIqqil7PkHtBhu9rstomWjWnMTDi4wFsJHQiPXpvm6mk6ouPqEdDe2d5xjbiRqIZw1DD6IKBlpbnzyiQtGKNMD245KtBgxkRYjW52INWk0fWz6gc087wnaJx0yLwj2yE_AHqz7oSUB30V3UcyjJtjwmJCYlzWhFWUVpQlGOM7jCuMgwPB-etpUE5BkYyIhQACBesoKcqWtmUnOlzmmBDOSNBM0HbfTejhpyngMWC2wBSyLJMFzTOcZfMMA3BRkDbPJSZAJefzBbvsmYI-eH8eqzQVVkLya0vaT0raZBSDASXBpYd6Ha-t1srHeyVO4OINaBtyF5T---vnFw8A5mHIAQAA
   *
   * <p>Of course, the nice solution would be to wrap all potentially offensive data in cdata tags.
   * However, that is not an optimal solution because I don't know of a way to change the CData
   * encoding back to normal encoding when writing to the dcc database. This might be an interesting
   * problem for further investigation into how dcc etl jobs work and what options there are in the
   * TTL to modify incoming data.
   *
   * @param input UTF-16 String meant as content of an xml tag or attribute
   * @return The input string with illegal xml sequences removed
   */
  public static String stripIllegalXml(String input) {
    if (input == null) {
      return null;
    }
    // this is taken straight from
    // https://stackoverflow.com/questions/4237625/removing-invalid-xml-characters-from-a-string-in-java
    // XML 1.0
    // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
    String pattern =
        "[^" + "\u0009\r\n" + "\u0020-\uD7FF" + "\uE000-\uFFFD" + "\ud800\udc00-\udbff\udfff" + "]";

    return input.replaceAll(pattern, "");
  }
}
