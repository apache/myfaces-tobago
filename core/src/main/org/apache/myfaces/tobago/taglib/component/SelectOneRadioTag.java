/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class SelectOneRadioTag extends SelectOneTag
    implements org.apache.myfaces.tobago.taglib.decl.SelectOneRadioTag {

  private String renderRange;

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange, getIterationHelper());
  }

  public void release() {
    super.release();
    renderRange = null;
  }

  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }

}

