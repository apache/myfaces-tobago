/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 14.08.2002 at 14:39:25.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.component.UIFile;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

public class FileTag extends InputTag {

  private boolean multiple;

  public String getComponentType() {
    return UIFile.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    if (multiple) {
      setProperty(component, TobagoConstants.ATTR_MULTIPLE_FILES, Boolean.TRUE);
    }
  }

  public int doStartTag() throws JspException {
    int ret = super.doStartTag();
    UIPage form = ComponentUtil.findPage(getComponentInstance());
    form.getAttributes().put(TobagoConstants.ATTR_ENCTYPE, "multipart/form-data");

    return ret;

  }

  public boolean isMultiple() {
    return multiple;
  }

  public void setMultiple(boolean multiple) {
    this.multiple = multiple;
  }
}
