/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 14.08.2002 at 14:39:25.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

import javax.servlet.jsp.JspException;

/**
 * Renders a file input field. 
 */
@Tag(name="file", bodyContent="JSP=")
public class FileTag extends InputTag
    implements HasIdBindingAndRendered, HasValue, IsDisabled,
               HasLabelAndAccessKey, HasOnchangeListener, HasTip {
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

