/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved. Created 20.08.2004 09:22:40.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.ViewHandler;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import java.util.Set;

public class Window {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Window.class);

// ///////////////////////////////////////////// attribute

  private String viewId;
  private String name;
  private int width = 300;
  private int height = 300;
  private int left = 100;
  private int top = 100;
  private boolean dependent;

// ///////////////////////////////////////////// constructor

  public Window(String viewId) {
    this.viewId = viewId;
  }

  public Window(String viewId, int width, int height) {
    this.viewId = viewId;
    this.width = width;
    this.height = height;
  }

  public Window(String viewId, int width, int heigth, int x, int y) {
    this.viewId = viewId;
    this.width = width;
    this.height = heigth;
    this.left = x;
    this.top = y;
  }

// ///////////////////////////////////////////// code

  public void activate(UIPage page) {
    page.getScriptFiles().add("script/popup.js");

    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    String actionUrl = viewHandler.getActionURL(facesContext, viewId);
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionUrl = '" + actionUrl + "'");
    }

    StringBuffer buffer = new StringBuffer();
    buffer.append("openPopup('");
    buffer.append(actionUrl);
    buffer.append("', '");
    buffer.append(name);
    buffer.append("', '");
    buffer.append(width);
    buffer.append("', '");
    buffer.append(height);
    buffer.append("', '");
    if (dependent) {
      buffer.append('p');
    }
    buffer.append("', '");
    buffer.append(left);
    buffer.append("', '");
    buffer.append(top);
    buffer.append("');");
    page.getOnloadScripts().add(buffer.toString());
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

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getTop() {
    return top;
  }

  public void setTop(int top) {
    this.top = top;
  }

  public boolean isDependent() {
    return dependent;
  }

  public void setDependent(boolean dependent) {
    this.dependent = dependent;
  }
}

