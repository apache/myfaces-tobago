/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 19.08.2002 at 16:07:05.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SERVER_SIDE_TABS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasState;

import javax.faces.component.UIComponent;

/**
 * Renders a tabpanel.
 */
@Tag(name = "tabGroup")
@BodyContentDescription(anyTagOf = "(<tc:tab>* ")
public class TabGroupTag extends TobagoTag
    implements HasIdBindingAndRendered, HasDimension, HasState {

  private String serverside;
  private String state;

  public String getComponentType() {
    return UITabGroup.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setBooleanProperty(component, ATTR_SERVER_SIDE_TABS, serverside);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
  }

  public void release() {
    super.release();
    serverside = null;
    state = null;
  }

  public String getServerside() {
    return serverside;
  }


  /**
   * Flag indicating that tab switching is done by server request.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "false")
  public void setServerside(String serverside) {
    this.serverside = serverside;
  }

  public void setState(String state) {
    this.state = state;
  }
}

