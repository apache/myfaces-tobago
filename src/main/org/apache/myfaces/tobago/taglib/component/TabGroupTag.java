/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 19.08.2002 at 16:07:05.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasState;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

/**
 * Renders a tabpanel.
 */
@Tag(name="tabGroup")
@BodyContentDescription(anyTagOf="(<t:tab>* " )
public class TabGroupTag extends TobagoTag
    implements HasIdBindingAndRendered, HasDimension, HasState
    {
// ----------------------------------------------------------------- attributes

  private String serverside;
  private String state;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITabGroup.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setBooleanProperty(component, ATTR_SERVER_SIDE_TABS, serverside, getIterationHelper());

    // todo: check, if it is an writeable object
    if (state != null && isValueReference(state)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(state, getIterationHelper());
      component.setValueBinding(ATTR_STATE, valueBinding);
    }
  }

  public void release() {
    super.release();
    serverside = null;
    state = null;
  }

// ------------------------------------------------------------ getter + setter

  public String getServerside() {
    return serverside;
  }


  /**
   * Flag indicating that tab switching is done by server request.
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
  public void setServerside(String serverside) {
    this.serverside = serverside;
  }

  public void setState(String state) {
    this.state = state;
  }
}

