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

package org.apache.myfaces.tobago.taglib.sandbox;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MODE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ICONS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_JUNCTIONS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROOT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROOT_JUNCTION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.taglib.component.TobagoTag;

import javax.faces.component.UIComponent;

public class TreeTag extends TobagoTag implements TreeTagDeclaration {

  private String value;

  private String showJunctions;
  private String showIcons;
  private String showRoot;
  private String showRootJunction;

  private String selectable;

  private String required;

  private String mode;

  @Override
  public String getComponentType() {
    return UITree.COMPONENT_TYPE;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);

    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_JUNCTIONS, showJunctions);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_ICONS, showIcons);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_ROOT, showRoot);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_ROOT_JUNCTION, showRootJunction);

    ComponentUtil.setStringProperty(component, ATTR_SELECTABLE, selectable);

    ComponentUtil.setBooleanProperty(component, ATTR_REQUIRED, required);
    ComponentUtil.setStringProperty(component, ATTR_MODE, mode);
  }

  @Override
  public void release() {
    super.release();
    value = null;
    showJunctions = null;
    showIcons = null;
    showRoot = null;
    showRootJunction = null;
    selectable = null;
    required = null;
    mode = null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getShowIcons() {
    return showIcons;
  }

  public void setShowIcons(String showIcons) {
    this.showIcons = showIcons;
  }

  public String getShowJunctions() {
    return showJunctions;
  }

  public void setShowJunctions(String showJunctions) {
    this.showJunctions = showJunctions;
  }

  public String getShowRoot() {
    return showRoot;
  }

  public void setShowRoot(String showRoot) {
    this.showRoot = showRoot;
  }

  public String getShowRootJunction() {
    return showRootJunction;
  }

  public void setShowRootJunction(String showRootJunction) {
    this.showRootJunction = showRootJunction;
  }

  public String getSelectable() {
    return selectable;
  }

  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }
}
