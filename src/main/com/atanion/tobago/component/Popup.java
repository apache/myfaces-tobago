/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 20.08.2004 09:22:40.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.ViewHandler;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import java.util.Set;

public class Popup {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Popup.class);

// ///////////////////////////////////////////// attribute

  private String viewId;
  private String name;
  private int width = 300;
  private int heigth = 300;
  private int x = 100;
  private int y = 100;
  private boolean dependent;

// ///////////////////////////////////////////// constructor

  public Popup(String viewId) {
    this.viewId = viewId;
  }

  public Popup(String viewId, int width, int heigth) {
    this.viewId = viewId;
    this.width = width;
    this.heigth = heigth;
  }

  public Popup(String viewId, int width, int heigth, int x, int y) {
    this.viewId = viewId;
    this.width = width;
    this.heigth = heigth;
    this.x = x;
    this.y = y;
  }

// ///////////////////////////////////////////// code

  public void activate(UIPage page) {
    page.getScriptFiles().add("popup.js", true);
    Set scripts = page.getOnloadScripts();

    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    String actionUrl = viewHandler.getActionURL(facesContext, viewId);
    LOG.debug("actionUrl = '" + actionUrl + "'");

    StringBuffer buffer = new StringBuffer();
    buffer.append("openPopup('");
    buffer.append(actionUrl);
    buffer.append("', '");
    buffer.append(name);
    buffer.append("', '");
    buffer.append(width);
    buffer.append("', '");
    buffer.append(heigth);
    buffer.append("', '");
    if (dependent) {
      buffer.append('p');
    }
    buffer.append("', '");
    buffer.append(x);
    buffer.append("', '");
    buffer.append(y);
    buffer.append("');");
    scripts.add(buffer.toString());
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getViewId() {
    return viewId;
  }

  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeigth() {
    return heigth;
  }

  public void setHeigth(int heigth) {
    this.heigth = heigth;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean isDependent() {
    return dependent;
  }

  public void setDependent(boolean dependent) {
    this.dependent = dependent;
  }
}

