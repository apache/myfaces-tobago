/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 14:50:02.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.component.UIPopup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.List;

public class PopupTag extends TobagoBodyTag {

  private static final Log LOG = LogFactory.getLog(PopupTag.class);
  private String width;
  private String height;
  private String left;
  private String top;

  public String getComponentType() {
    return UIPopup.COMPONENT_TYPE;
  }

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    UIComponent component = getComponentInstance();
    UIPage page = ComponentUtil.findPage(component);
    List popups = (List) page.getAttributes().get(ATTR_POPUP_LIST);
    if (popups == null) {
      popups = new ArrayList();
      page.getAttributes().put(ATTR_POPUP_LIST, popups);
    }
    popups.add(component);
    return result;
  }

 /* protected String getFacetName() {
    String name = "tobagoPopup";
    int idx = 0;
    while (getParentUIComponentTag(pageContext).getComponentInstance().
        getFacet(name + idx) != null) {
      idx++;
    }
    return name + idx;
  }*/

  public void release() {
    super.release();
    width = null;
    height = null;
    left = null;
    top = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LEFT, left, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TOP, top, getIterationHelper());
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public void setTop(String top) {
    this.top = top;
  }

}

