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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIDatePicker;

import javax.faces.component.UIComponent;


/*
 * Date: 30.05.2006
 * Time: 19:11:07
 */
public class DatePickerTag extends AbstractCommandTag implements DatePickerTagDeclaration {

  private String forComponent;
  private String tabIndex;

  @Override
  public String getComponentType() {
    return UIDatePicker.COMPONENT_TYPE;
  }

  @Override
  public void release() {
    super.release();
    forComponent = null;
    tabIndex = null;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
    ComponentUtil.setIntegerProperty(component, ATTR_TAB_INDEX, tabIndex);
  }

  public String getFor() {
    return forComponent;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  public String getTabIndex() {
    return tabIndex;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }
}
