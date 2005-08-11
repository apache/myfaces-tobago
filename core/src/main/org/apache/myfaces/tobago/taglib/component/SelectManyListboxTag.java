/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 17:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectMany;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;



public class SelectManyListboxTag extends SelectManyTag
    implements org.apache.myfaces.tobago.taglib.decl.SelectManyListboxTag {

  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    UIComponent facet = component.getFacet(FACET_LAYOUT);
    if (facet == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  }
}
