package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;

/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */
public class CellTag extends Panel_GroupTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CellTag.class);

// ///////////////////////////////////////////// attribute

  private String spanX = "1";

  private String spanY = "1";

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int doStartTag() throws JspException {
    return super.doStartTag();
  }

  public String getRendererType() {
    return "Panel_Group";
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    Application application = FacesContext.getCurrentInstance().getApplication();
    if (isValueReference(spanX)) {
      component.setValueBinding(TobagoConstants.ATTR_SPAN_X,
          application.createValueBinding(spanX));
    } else {
      setProperty(component, TobagoConstants.ATTR_SPAN_X, spanX);
    }

    if (isValueReference(spanY)) {
      component.setValueBinding(TobagoConstants.ATTR_SPAN_Y, 
          application.createValueBinding(spanY));
    } else {
      setProperty(component, TobagoConstants.ATTR_SPAN_Y, spanY);
    }

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

  public String getSpanX() {
    return spanX;
  }

  public void setSpanX(String spanX) {
    this.spanX = spanX;
  }

  public String getSpanY() {
    return spanY;
  }

  public void setSpanY(String spanY) {
    this.spanY = spanY;
  }

}
