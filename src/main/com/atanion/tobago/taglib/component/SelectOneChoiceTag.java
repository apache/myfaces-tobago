/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

public class SelectOneChoiceTag extends SelectOneTag
    implements com.atanion.tobago.taglib.decl.SelectOneChoiceTag {

  public int doEndTag() throws JspException {

    UIComponent component = getComponentInstance();
    if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  }

  public void setRequired(String required) {
    super.setRequired(required);
  }
}
