/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.01.2004 10:34:32.
 * $Id$
 */
package com.atanion.tobago.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

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

  private static final Set EMPTY_TAG
      = new HashSet(Arrays.asList(new String[]{
        "br", "area", "link", "img", "param", "hr", "input", "col", "base",
        "meta"}));


// ///////////////////////////////////////////// attribute

  private Writer writer;
  private UIComponent component;
  private boolean startStillOpen;
  private String contentType;
  private String characterEncoding;
  private Stack stack;
  private boolean xmlStylishEmptyTags;

// ///////////////////////////////////////////// constructor

  public TobagoResponseWriter(Writer writer, String contentType,
      String characterEncoding) {
    this.writer = writer;
    this.stack = new Stack();
    this.contentType = contentType;
    this.characterEncoding = characterEncoding;
    if ("application/xhtml".equals(contentType)
      || "application/xml".equals(contentType)
      || "text/xml".equals(contentType)) {
      xmlStylishEmptyTags = true;
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
        LOG.error("Don't know what to do! " +
            "Property defined, but no component to get a value. "
          + trace.substring(trace.indexOf('(')));
        LOG.error("value = '" + value + "'");
        LOG.error("property = '" + property + "'");
        return null;
      }
    } else {
      String trace = new Exception().getStackTrace()[2].toString();
      LOG.error("Don't know what to do! " +
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
    write(findValue(text, property));
  }

  public void writeText(char text[], int offset, int length) throws IOException {
    if (startStillOpen) {
      writer.write("\n>");
      startStillOpen = false;
    }
    writer.write(text, offset, length);
  }

  public void startDocument() throws IOException {
    // nothing to do
  }

  public void endDocument() throws IOException {
    close();
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
//    char firstChar = name.charAt(0);
//    if((firstChar == 's' || firstChar == 'S') && ("script".equalsIgnoreCase(name) || "style".equalsIgnoreCase(name)))
//    {
//        dontEscape = true;
//    }
    LOG.debug("start Element: " + name);
    if (startStillOpen) {
      writer.write("\n>");
    }
    writer.write("<");
    writer.write(name);
    startStillOpen = true;
  }

  public void endElement(String name) throws IOException {
    LOG.debug("end Element: " + name);

    String top = (String) stack.pop();
    if (! top.equals(name)) {
      String trace = new Exception().getStackTrace()[1].toString();
      LOG.error("Element end with name='" + name + "' doesn't "
          + "match with top element on the stack='" + top + "' "
          + trace.substring(trace.indexOf('(')));
    }

    if (EMPTY_TAG.contains(name)) {
      if (xmlStylishEmptyTags) {
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
      LOG.warn("Comment must not contain the sequence '--', comment = '"
          + comment + "' " + trace.substring(trace.indexOf('(')));
      write(StringUtils.replace(comment, "--", "++"));
    }
    write("-->");
  }

  public ResponseWriter cloneWithWriter(Writer writer) {
    return new TobagoResponseWriter(
        writer,  getContentType(), getCharacterEncoding());
  }

  public void writeAttribute(String name, boolean on) throws IOException {
    if (on) {
      writeAttribute(name, name, null);
    }
  }

  public void writeAttribute(String name, Object value, String property)
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
      writer.write(attribute);
      writer.write('\"');
    }
  }

  public void writeURIAttribute(String s, Object obj, String s1)
      throws IOException {
    LOG.error("Not implemented yet!"); // fixme jsfbeta
  }

// ///////////////////////////////////////////// bean getter + setter

}
