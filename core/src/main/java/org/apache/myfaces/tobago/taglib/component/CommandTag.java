package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_SCRIPT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_NAVIGATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMMEDIATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TYPE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;

import javax.faces.component.UIComponent;

public abstract class CommandTag extends TobagoTag implements CommandTagDeclaration {

  private String disabled;
  private String action;
  private String actionListener;
  private String type;
  private String immediate;
  private String script;
  private String navigate;

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
    ComponentUtil.setAction(component, type, action);
    ComponentUtil.setStringProperty(component, ATTR_ACTION_NAVIGATE, navigate);
    ComponentUtil.setStringProperty(component, ATTR_ACTION_SCRIPT, script);
    ComponentUtil.setActionListener(command, actionListener);
  }

  public void release() {
    super.release();
    action = null;
    actionListener = null;
    type = null;
    disabled = null;
    immediate = null;
    script = null;
    navigate = null;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public void setNavigate(String navigate) {
    this.navigate = navigate;
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
}
