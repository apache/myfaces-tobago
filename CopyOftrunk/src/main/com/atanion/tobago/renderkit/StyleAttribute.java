/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 30, 2002 at 2:55:19 PM.
 * $Id$
 */
package com.atanion.tobago.renderkit;

public class StyleAttribute {

// ///////////////////////////////////////////// attribute

  private StringBuffer content;

  public StyleAttribute() {
    content = new StringBuffer();
  }

  public StyleAttribute(String content) {
    this();
    if (content != null) {
      this.content.append(content);
      this.content.append(" ");
    }
  }

// ///////////////////////////////////////////// code

  public void add(String key, String value) {
    content.append(key);
    content.append(": ");
    content.append(value);
    content.append("; ");
  }

// ///////////////////////////////////////////// from Object

  public String toString() {
    return content.toString();
  }
}
