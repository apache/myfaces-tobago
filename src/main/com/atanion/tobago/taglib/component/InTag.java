/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;
import com.atanion.util.annotation.UIComponentTagAttribute;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;

import javax.faces.component.UIComponent;
@Tag(name="in")
public class InTag extends InputTag {

// ----------------------------------------------------------------- attributes

  private String password;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

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
   * Is rendered as password, so you will not see the typed charakters.
   * @param password
   */
  @UIComponentTagAttribute(type = Boolean.class)
  @TagAttribute
  public void setPassword(String password) {
    this.password = password;
  }
}
