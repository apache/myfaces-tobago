package org.apache.myfaces.tobago.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SWITCH_TYPE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITabGroup;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_CLIENT;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_RELOAD_PAGE;

import javax.faces.component.UIComponent;

public class TabGroupTag extends TobagoTag
    implements TabGroupTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TabGroupTag.class);

  private String state;
  private String switchType;

  @Override
  public String getComponentType() {
    return UITabGroup.COMPONENT_TYPE;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
    ComponentUtil.setStringProperty(component, ATTR_SWITCH_TYPE, switchType);
  }

  @Override
  public void release() {
    super.release();
    state = null;
  }

  public void setServerside(String serverside) {
    LOG.warn("Attribute 'serverside' is deprecated! Use 'switchType' instead.");
    this.switchType = Boolean.valueOf(serverside)
        ? SWITCH_TYPE_RELOAD_PAGE : SWITCH_TYPE_CLIENT;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setSwitchType(String switchType) {
    this.switchType = switchType;
  }
}

