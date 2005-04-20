/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

/**
 * Renders a text input field.
 */
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasConverter;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsFocus;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsReadOnly;
import com.atanion.tobago.taglib.decl.IsRequired;
import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
@Tag(name="in")
@BodyContentDescription(anyTagOf="facestag")
public class InTag extends TextInputTag
    implements HasValue, HasIdBindingAndRendered, HasConverter, IsReadOnly,
               IsDisabled, HasWidth, HasOnchangeListener, IsInline, IsFocus,
               IsRequired, HasTip, HasLabelAndAccessKey {

// ----------------------------------------------------------------- attributes

  private String password;


// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    password = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setBooleanProperty(component, ATTR_PASSWORD, password,
        getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getPassword() {
    return password;
  }


  /**
   * Flag indicating whether or not this component should be rendered as
   * password field , so you will not see the typed charakters.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = Boolean.class, defaultValue = "false")
  public void setPassword(String password) {
    this.password = password;
  }
}
