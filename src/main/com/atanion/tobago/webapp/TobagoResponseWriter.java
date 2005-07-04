/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.01.2004 10:34:32.
 * $Id$
 */
package com.atanion.tobago.webapp;

import com.atanion.xml.XmlUtils;
import com.atanion.tobago.util.HtmlWriterUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


public class TobagoResponseWriter extends ResponseWriter {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TobagoResponseWriter.class);

  private static final Set<String> EMPTY_TAG
      = new HashSet<String>(
          Arrays.asList(
              new String[]{
                "br", "area", "link", "img", "param", "hr", "input", "col", "base",
                "meta"}));


// ///////////////////////////////////////////// attribute

  private Writer writer;

  private UIComponent component;

  private boolean startStillOpen;

  private String contentType;

  private String characterEncoding;

  private Stack<String>  stack;

  /** use XML instead HMTL */
  private boolean xml;

  private boolean insideScriptOrStyle = false;

// ///////////////////////////////////////////// constructor

  public TobagoResponseWriter(
      Writer writer, String contentType, String characterEncoding) {
    this.writer = writer;
    this.stack = new Stack<String>();
    this.contentType = contentType;
    this.characterEncoding = characterEncoding;
    if ("application/xhtml".equals(contentType)
        || "application/xml".equals(contentType)
        || "text/xml".equals(contentType)) {
      xml = true;
    }
  }

// ///////////////////////////////////////////// code

  private String findValue(Object value, String property) {
    if (value != null) {
      return value.toString();
    } else if (property != null) {
      if (component != null) {
        Object object = component.getAttributes().get(property);
        if (object != null) {
          return object.toString();
        } else {
          return null;
        }
      } else {
        String trace = new Exception().getStackTrace()[2].toString();
        LOG.error(
            "Don't know what to do! " +
            "Property defined, but no component to get a value. "
            + trace.substring(trace.indexOf('(')));
        LOG.error("value = '" + value + "'");
        LOG.error("property = '" + property + "'");
        return null;
      }
    } else {
      String trace = new Exception().getStackTrace()[2].toString();
      LOG.error(
          "Don't know what to do! " +
          "No value and no property defined. "
          + trace.substring(trace.indexOf('(')));
      LOG.error("value = '" + value + "'");
      LOG.error("property = '" + property + "'");
      return null;
    }
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    writer.write(cbuf, off, len);
  }

  public void write(String str) throws IOException {
    if (startStillOpen) {
      writer.write("\n>");
      startStillOpen = false;
    }
    super.write(str);
  }

  public void close() throws IOException {
    writer.close();
  }

  public void flush() throws IOException {
    writer.flush();
  }

  public void writeText(Object text, String property) throws IOException {
    if (startStillOpen) {
      writer.write("\n>");
      startStillOpen = false;
    }
    String value = findValue(text, property);
    if (insideScriptOrStyle) {
      write(value);
    } else {
      if (xml) {
        write(XmlUtils.escape(value));
      } else {
        HtmlWriterUtil.writeText(writer, value);
      }
    }
  }

  public void writeText(char text[], int offset, int length)
      throws IOException {
    if (startStillOpen) {
      writer.write("\n>");
      startStillOpen = false;
    }
    if (insideScriptOrStyle) {
      writer.write(text, offset, length);
    } else {
      if (xml) {
        writer.write(XmlUtils.escape(text.toString()).toCharArray(), offset, length);
// fixme: not nice:     XmlUtils.escape(text.toString()).toCharArray()
      } else {
        HtmlWriterUtil.writeText(writer, text, offset, length);
      }
    }

  }

  public void startDocument() throws IOException {
    // nothing to do
  }

  public void endDocument() throws IOException {
    // nothing to do
  }

  public String getContentType() {
    return contentType;
  }

  public String getCharacterEncoding() {
    return characterEncoding;
  }

  public void startElement(String name, UIComponent component)
      throws IOException {
    this.component = component;
    stack.push(name);
//    closeStartIfNecessary();
    char firstChar = name.charAt(0);
    if ((firstChar == 's' || firstChar == 'S')
        && ("script".equalsIgnoreCase(name) || "style".equalsIgnoreCase(name))) {
      insideScriptOrStyle = true;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("start Element: " + name);
    }
    if (startStillOpen) {
      writer.write("\n>");
    }
    writer.write("<");
    writer.write(name);
    startStillOpen = true;
  }

  public void endElement(String name) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("end Element: " + name);
    }

    String top = stack.pop();
    if (!top.equals(name)) {
      String trace = new Exception().getStackTrace()[1].toString();
      LOG.error(
          "Element end with name='" + name + "' doesn't "
          + "match with top element on the stack='" + top + "' "
          + trace.substring(trace.indexOf('(')));
    }

    insideScriptOrStyle = false;

    if (EMPTY_TAG.contains(name)) {
      if (xml) {
        writer.write("\n/>");
      } else {
        writer.write("\n>");
      }
    } else {
      if (startStillOpen) {
        writer.write("\n>");
      }
      writer.write("</");
      writer.write(name);
//      writer.write("\n>"); // fixme: this makes problems with Tidy
      writer.write(">");
    }
    startStillOpen = false;
  }

  public void writeComment(Object obj) throws IOException {
    if (startStillOpen) {
      writer.write("\n>");
      startStillOpen = false;
    }
    String comment = obj.toString();
    write("<!--");
    if (comment.indexOf("--") < 0) {
      write(comment);
    } else {
      String trace = new Exception().getStackTrace()[1].toString();
      LOG.warn(
          "Comment must not contain the sequence '--', comment = '"
          + comment + "' " + trace.substring(trace.indexOf('(')));
      write(StringUtils.replace(comment, "--", "++"));
    }
    write("-->");
  }

  public ResponseWriter cloneWithWriter(Writer writer) {
    return new TobagoResponseWriter(
        writer, getContentType(), getCharacterEncoding());
  }

  public void writeAttribute(String name, boolean on) throws IOException {
    if (on) {
      // boolean attributes don't need escaped
      writeAttribute(name, name, false);
    }
  }

  public void writeComponentAttribute(String name, String property)
      throws IOException {
    writeAttribute(name, null, property, true);
  }

  public void writeAttribute(String name, Object value, String property)
      throws IOException {
    writeAttribute(name, value, property, true);
  }

  public void writeAttribute(String name, Object value, boolean escape)
      throws IOException {
      writeAttribute(name, value.toString(), escape);
  }
  public void writeAttribute(String name, String value, boolean escape)
      throws IOException {
    if (!startStillOpen) {
      String trace = new Exception().getStackTrace()[1].toString();
      String error = "Cannot write attribute when start-tag not open. "
          + "name = '" + name + "'"
          + "value = '" + value + "' "
          + trace.substring(trace.indexOf('('));
      LOG.error(error);
      throw new IllegalStateException(error);
    }

    if (value != null) {
      writer.write(' ');
      writer.write(name);
      writer.write("=\"");
      if (xml) {
        writer.write(XmlUtils.escape(value));
      } else {
        if (escape && HtmlWriterUtil.attributeValueMustEscaped(name)) {
          HtmlWriterUtil.writeAttributeValue(writer,value);
        } else {
          writer.write(value);
        }
      }
      writer.write('\"');
    }
  }

  public void writeAttribute(String name, Object value, String property, boolean escape)
      throws IOException {
    if (!startStillOpen) {
      String trace = new Exception().getStackTrace()[1].toString();
      String error = "Cannot write attribute when start-tag not open. "
          + "name = '" + name + "'"
          + "value = '" + value + "'"
          + "property = '" + property + "' "
          + trace.substring(trace.indexOf('('));
      LOG.error(error);
      throw new IllegalStateException(error);
    }

    String attribute = findValue(value, property);
    if (attribute != null) {
      writer.write(' ');
      writer.write(name);
      writer.write("=\"");
      if (xml) {
        writer.write(XmlUtils.escape(attribute));
      } else {
        if (escape && HtmlWriterUtil.attributeValueMustEscaped(name)) {
          HtmlWriterUtil.writeAttributeValue(writer,attribute);
        } else {
          writer.write(attribute);
        }
      }
      writer.write('\"');
    }
  }



  public void writeIdAttribute(String id) throws IOException {
    writeAttribute("id", id, false);
  }

  public void writeNameAttribute(String id) throws IOException {
    writeAttribute("name", id, false);
  }

  public void writeClassAttribute(String id) throws IOException {
    writeAttribute("class", id, false);
  }

  public void writeComponentId(String property) throws IOException {
    writeComponentAttribute("id", property);
  }

  public void writeComponentName(String property) throws IOException {
    writeComponentAttribute("name", property);
  }

  public void writeComponentClass(String property) throws IOException {
    writeComponentAttribute("class", property);
  }



  public void writeURIAttribute(String s, Object obj, String s1)
      throws IOException {
    LOG.error("Not implemented yet!"); // fixme jsfbeta
  }

// ///////////////////////////////////////////// bean getter + setter

}
