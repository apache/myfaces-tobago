/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

public class RichTextEditorTag extends TextInputTag
    implements com.atanion.tobago.taglib.decl.RichTextEditorTag {

  private String statePreview;


  public int doEndTag() throws JspException {
    // todo: own layout for editor? 
    int result = super.doEndTag();
    getComponentInstance().getFacets().remove(FACET_LAYOUT);
    return result;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setBooleanProperty(component, ATTR_STATE_PREVIEW, statePreview, getIterationHelper());
  }

  public void release() {
    super.release();
    statePreview = null;
  }

  public String getStatePreview() {
    return statePreview;
  }

  public void setStatePreview(String statePreview) {
    this.statePreview = statePreview;
  }
}
