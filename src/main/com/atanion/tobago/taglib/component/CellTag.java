package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */
public class CellTag extends Panel_GroupTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CellTag.class);

// ///////////////////////////////////////////// attribute

  int spanX = 1;

  int spanY = 1;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getRendererType() {
    return "Panel_Group";
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_SPAN_X, new Integer(spanX) );
    setProperty(component, TobagoConstants.ATTR_SPAN_Y, new Integer(spanY) );
    setProperty(component, TobagoConstants.ATTR_LAYOUT_DIRECTIVE, Boolean.TRUE);
    if (LOG.isDebugEnabled()) {
      LOG.debug("spanx=" + spanX + " spanY=" + spanY);
      LOG.debug("spanx=" +
          component.getAttributes().get(TobagoConstants.ATTR_SPAN_X)
          + " spanY=" +
          component.getAttributes().get(TobagoConstants.ATTR_SPAN_Y));
      LOG.debug("component = " + getComponentInstance());
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public int getSpanX() {
    return spanX;
  }

  public void setSpanX(int spanX) {
    this.spanX = spanX;
  }

  public int getSpanY() {
    return spanY;
  }

  public void setSpanY(int spanY) {
    this.spanY = spanY;
  }

}
