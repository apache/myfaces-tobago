package com.atanion.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */

/**
 * Renders a layout cell.
 * A panel with ability to span over more than one layout cells.
 */
@Tag(name="cell", bodyContent="JSP")
public class CellTag extends TobagoBodyTag implements HasIdBindingAndRendered {

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

    /**
   *  <![CDATA[
   *   Count of layout column's to span over.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="1")
  public void setSpanX(String spanX) {
    this.spanX = spanX;
  }

  public String getSpanY() {
    return spanY;
  }

  /**
   *  <![CDATA[
   *   Count of layout row's to span over.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="1")
  public void setSpanY(String spanY) {
    this.spanY = spanY;
  }

  public String getScrollbars() {
    return scrollbars;
  }


  /**
   *  <![CDATA[
   *  possible values are:
   *    'false' : no scrollbars should rendered
   *    'true'  : scrollbars should always rendered
   *    'auto'  : scrollbars should rendered when needed
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class, defaultValue="false")
  public void setScrollbars(String scrollbars) {
    this.scrollbars = scrollbars;
  }
}
