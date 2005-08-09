/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 20.12.2004 11:13:35.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;

public class UIPopup extends UIPanel implements NamingContainer {

  private static final Log LOG = LogFactory.getLog(UIPopup.class);

  private String width;
  private String height;
  private String left;
  private String top;

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Popup";

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getTop() {
    return top;
  }

  public void setTop(String top) {
    this.top = top;
  }
}
