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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VAR;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITreeData;
import org.apache.myfaces.tobago.taglib.component.TobagoTag;

import javax.faces.component.UIComponent;

public class TreeDataTag extends TobagoTag implements TreeDataTagDeclaration {

  private String value;
  private String var;

  @Override
  public String getComponentType() {
    return UITreeData.COMPONENT_TYPE;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(component, ATTR_VAR, var);
  }

  @Override
  public void release() {
    super.release();
    value = null;
    var = null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }
}
