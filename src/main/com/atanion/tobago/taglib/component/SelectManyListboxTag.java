/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasHeight;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.component.UISelectMany;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.util.annotation.Tag;

import javax.servlet.jsp.JspException;
import javax.faces.component.UIComponent;


/**
 * Render a multi selection option listbox.
 */
@Tag(name="selectManyListbox")
public class SelectManyListboxTag extends SelectManyTag
    implements HasId, HasValue, IsDisabled, HasHeight, IsInline,
               HasLabelAndAccessKey, IsRendered, HasBinding, HasTip
    {

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
