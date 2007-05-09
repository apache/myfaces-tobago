package org.apache.myfaces.tobago.webapp;

import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * User: lofwyr
 * Date: 08.05.2007 13:51:43
 */
public interface OptimizedResponseWriter {

  // same as in ResponseWriter

  void startElement(String name, UIComponent component) throws IOException;

  void endElement(String name) throws IOException;

  // others (not from ResponseWriter)

  /**
   * Writes a string attribute. The renderer may set escape=false to switch of escaping of the string,
   * if it is not necessary.
   */
  void writeAttribute(String name, String string, boolean escape) throws IOException;

  /**
   * Writes a boolean attribute. The value will not escaped.
   */
  void writeAttribute(String name, boolean on) throws IOException;

  /**
   * Writes a integer attribute. The value will not escaped.
   */
  void writeAttribute(String name, int number) throws IOException;

  /**
   * Write the id attribute. The value will not escaped.
   */
  void writeIdAttribute(String id) throws IOException;

  /**
   * Write the name attribute. The value will not escaped.
   */
  void writeNameAttribute(String name) throws IOException;

  /**
   * Write the class attribute. The value will not escaped.
   */
  void writeClassAttribute(String cssClass) throws IOException;

  /**
   * Write the class attribute. The value will not escaped.
   */
  void writeClassAttribute() throws IOException;

  /**
   * Write the style attribute. The value will not escaped.
   */
  void writeStyleAttribute(String style) throws IOException;

  /**
   * Write the style attribute. The value will not escaped.
   */
  void writeStyleAttribute() throws IOException;

  void writeJavascript(String script) throws IOException;

  void writeText(String text) throws IOException;
}
