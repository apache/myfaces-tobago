/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 14.08.2002 at 14:39:25.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.servlet.jsp.JspException;

public class FileTag extends InputTag {
// ----------------------------------------------------------- business methods

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    UIPage form = ComponentUtil.findPage(getComponentInstance());
    form.getAttributes().put(ATTR_ENCTYPE, "multipart/form-data");
    return result;
  }

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}

