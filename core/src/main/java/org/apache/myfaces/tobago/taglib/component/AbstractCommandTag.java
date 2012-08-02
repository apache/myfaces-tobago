/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMMEDIATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_JSF_RESOURCE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RESOURCE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TYPE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;

import javax.faces.component.UIComponent;

public abstract class AbstractCommandTag extends TobagoTag implements AbstractCommandTagDeclaration {

  private String disabled;
  private String action;
  private String actionListener;
  private String type;
  private String immediate;
  private String onclick;
  private String link;
  private String resource;
  private String jsfResource;
  private String transition;

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    UICommand command = (UICommand) component;
    ComponentUtil.setBooleanProperty(component, ATTR_DISABLED, disabled);
    ComponentUtil.setStringProperty(component, ATTR_TYPE, type);
//   ComponentUtil.setBooleanProperty(component, ATTR_DEFAULT_COMMAND, defaultCommand);
    ComponentUtil.setBooleanProperty(component, ATTR_IMMEDIATE, immediate);
    if (component instanceof UICommand) {
      ComponentUtil.setAction((UICommand) component, type, action);
    }
    ComponentUtil.setStringProperty(component, ATTR_ACTION_LINK, link);
    ComponentUtil.setStringProperty(component, ATTR_RESOURCE, resource);
    ComponentUtil.setBooleanProperty(component, ATTR_JSF_RESOURCE, jsfResource);
    ComponentUtil.setStringProperty(component, ATTR_ACTION_ONCLICK, onclick);
    ComponentUtil.setActionListener(command, actionListener);
    ComponentUtil.setBooleanProperty(component, ATTR_TRANSITION, transition);
  }

  public void release() {
    super.release();
    action = null;
    actionListener = null;
    type = null;
    disabled = null;
    immediate = null;
    onclick = null;
    link = null;
    resource = null;
    jsfResource = null;
    transition = null;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setOnclick(String onclick) {
    this.onclick = onclick;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public void setJsfResource(String jsfResource) {
    this.jsfResource = jsfResource;
  }

  public String getActionListener() {
    return actionListener;
  }

  public void setActionListener(String actionListener) {
    this.actionListener = actionListener;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getImmediate() {
    return immediate;
  }

  public void setImmediate(String immediate) {
    this.immediate = immediate;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setTransition(String transition) {
    this.transition = transition;
  }
}
