package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasScrollbars;
import com.atanion.tobago.taglib.decl.HasSpanXY;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */
@Tag(name="cell")
public class CellTag extends TobagoBodyTag
    implements HasId, IsRendered, HasBinding, HasSpanXY, HasScrollbars
    {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CellTag.class);

// ///////////////////////////////////////////// attribute

  private String spanX = "1";

  private String spanY = "1";

  private String scrollbars;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  public int doStartTag() throws JspException {
    return super.doStartTag();
  }

  public String getRendererType() {
    return RENDERER_TYPE_PANEL;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setIntegerProperty(component, ATTR_SPAN_X, spanX, getIterationHelper());
   ComponentUtil.setIntegerProperty(component, ATTR_SPAN_Y, spanY, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_SCROLLBARS, scrollbars, getIterationHelper());

    component.getAttributes().put(ATTR_LAYOUT_DIRECTIVE, Boolean.TRUE);

    if (LOG.isDebugEnabled()) {
      LOG.debug("spanX=" + spanX + " spanY=" + spanY);
      LOG.debug("spanX=" +
          component.getAttributes().get(ATTR_SPAN_X)
          + " spanY=" +
          component.getAttributes().get(ATTR_SPAN_Y));
      LOG.debug("component = " + getComponentInstance());
    }
  }

  public void release() {
    super.release();
    spanX = "1";
    spanY = "1";
    scrollbars = null;
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

  public String getScrollbars() {
    return scrollbars;
  }

  public void setScrollbars(String scrollbars) {
    this.scrollbars = scrollbars;
  }
}
