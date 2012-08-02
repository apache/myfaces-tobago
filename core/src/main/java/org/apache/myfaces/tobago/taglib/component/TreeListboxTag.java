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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP_REFERENCE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ID_REFERENCE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_NAME_REFERENCE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITreeListbox;

import javax.faces.component.UIComponent;

public class TreeListboxTag extends TobagoTag
    implements TreeListboxTagDeclaration {

  private String value;
  private String state;
  private String idReference;
  private String nameReference;
  private String tipReference;
  private String selectable;
  private String required;

  public String getComponentType() {
    return UITreeListbox.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
    ComponentUtil.setStringProperty(component, ATTR_ID_REFERENCE, idReference);
    ComponentUtil.setStringProperty(component, ATTR_NAME_REFERENCE, nameReference);
    ComponentUtil.setStringProperty(component, ATTR_SELECTABLE, selectable);
    ComponentUtil.setBooleanProperty(component, ATTR_REQUIRED, required);
    ComponentUtil.setStringProperty(component, ATTR_TIP_REFERENCE, tipReference);

  }

  public void release() {
    super.release();
    value = null;
    state = null;
    idReference = null;
    nameReference = null;
    tipReference = null;
    selectable = null;
    required = null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getIdReference() {
    return idReference;
  }

  public void setIdReference(String idReference) {
    this.idReference = idReference;
  }

  public String getNameReference() {
    return nameReference;
  }

  public void setNameReference(String nameReference) {
    this.nameReference = nameReference;
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

  public void setTipReference(String tipReference) {
    this.tipReference = tipReference;
  }
}

