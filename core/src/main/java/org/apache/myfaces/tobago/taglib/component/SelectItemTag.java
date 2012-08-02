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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ITEM_DESCRIPTION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ITEM_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ITEM_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ITEM_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ITEM_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectItem;

import javax.faces.component.UIComponent;

public class SelectItemTag extends TobagoTag implements SelectItemTagDeclaration {

  private String itemDescription;
  private String itemDisabled;
  private String itemLabel;
  private String itemValue;
  private String value;
  private String itemImage;
  private String markup;


  public String getComponentType() {
    return UISelectItem.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_ITEM_DESCRIPTION, itemDescription);
    ComponentUtil.setBooleanProperty(component, ATTR_ITEM_DISABLED, itemDisabled);
    ComponentUtil.setStringProperty(component, ATTR_ITEM_LABEL, itemLabel);
    ComponentUtil.setStringProperty(component, ATTR_ITEM_VALUE, itemValue);
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(component, ATTR_ITEM_IMAGE, itemImage);
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, itemImage);
    ComponentUtil.setMarkup(component, markup);
  }

  public void release() {
    super.release();
    itemDescription = null;
    itemDisabled = null;
    itemLabel = null;
    itemValue = null;
    value = null;
    itemImage = null;
    markup = null;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public String getItemDisabled() {
    return itemDisabled;
  }

  public void setItemDisabled(String itemDisabled) {
    this.itemDisabled = itemDisabled;
  }

  public String getItemLabel() {
    return itemLabel;
  }

  public void setItemLabel(String itemLabel) {
    this.itemLabel = itemLabel;
  }

  public String getItemValue() {
    return itemValue;
  }

  public void setItemValue(String itemValue) {
    this.itemValue = itemValue;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getItemImage() {
    return itemImage;
  }

  public void setItemImage(String itemImage) {
    this.itemImage = itemImage;
  }

  public String getMarkup() {
    return markup;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }
}
