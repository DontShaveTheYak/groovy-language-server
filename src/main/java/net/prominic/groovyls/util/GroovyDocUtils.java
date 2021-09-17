package net.prominic.groovyls.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

import groovy.lang.groovydoc.Groovydoc;

import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ASTNode;
import net.prominic.groovyls.groovydoc.GroovyDoc2MarkdownConverter;

/**
 * Functions for retrieving and formatting GroovyDoc strings.
 */
public class GroovyDocUtils {

  /**
   * Gets the Markdown formatted docString for a node, if the node doesn't have a
   * docString then an empty String is returned.
   *
   * @param node The AST node to check for a docString.
   * @return The docString or empty String.
   */
  public static String getDocString(final ASTNode node) {

    String docString = "";

    if (!(node instanceof AnnotatedNode)) {
      return docString;
    }

    AnnotatedNode docNode = (AnnotatedNode) node;

    final String rawDocString = getRawDocString(docNode);

    if (rawDocString == "") {
      return docString;
    }

    try {
      docString = new GroovyDoc2MarkdownConverter(rawDocString).getAsString();
    } catch (IOException e) {
      System.err.println("Failed to convert docString to markdown: " + e.toString());
    }

    return docString;

  }

  /**
   * Gets the raw docString for a node, if the node doesn't have a docString then
   * an empty String is returned.
   *
   * @param node The AST node to check for a docString.
   * @return The docString or empty String.
   */
  protected static String getRawDocString(AnnotatedNode node) {

    Groovydoc docstring = node.getGroovydoc();
    String content = "";

    if (docstring.isPresent()) {
      content = removeComment(docstring.getContent());
    }

    return content;
  }

  /**
   * Takes a commented block of text and removes the comments, leaving the content
   * as is.
   *
   * @param text The commented block of text.
   * @return The content from inside the comment block.
   */
  protected static String removeComment(String text) {

    String result;
    Pattern pattern;
    Matcher matcher;

    // Get the content from "* some content\n" to "some content\n"
    final String content = "^[\\h]*\\*\\h?(.*\\n)";
    final String cSubst = "$1";

    pattern = Pattern.compile(content, Pattern.MULTILINE);
    matcher = pattern.matcher(text);

    result = matcher.replaceAll(cSubst);

    // Strips the leading comment "/**""
    final String stripLeading = "^\\h?\\/\\*{2}\\s*";
    final String lSubst = "";

    pattern = Pattern.compile(stripLeading, Pattern.MULTILINE);
    matcher = pattern.matcher(result);

    result = matcher.replaceAll(lSubst);

    // Strips the trailing comment "\*"
    final String stripTrailing = "\\h?\\*\\/\\s?$";
    final String tSubst = "";

    pattern = Pattern.compile(stripTrailing, Pattern.MULTILINE);
    matcher = pattern.matcher(result);

    result = matcher.replaceAll(tSubst);

    return result;

  }

  /**
   * Wrap the supplied Groovy code in a markdown codeblock with
   * Groovy syntax high lighting.
   * <p>
   * A newline will be appended to the end of the returned String.
   * @param groovyCode The
   * @return The markdown codeblock that ends with a newline.
   */
  public static String useGroovyHighlighting(final String groovyCode) {
    final StringBuilder markdownContent = new StringBuilder();

    markdownContent.append("```groovy\n");
    markdownContent.append(groovyCode + "\n");
    markdownContent.append("```\n");

    return markdownContent.toString();
  }

}
