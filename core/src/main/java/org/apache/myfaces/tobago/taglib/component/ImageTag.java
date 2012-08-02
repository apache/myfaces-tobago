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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_BORDER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;

public class ImageTag extends TobagoTag implements ImageTagDeclaration {

  private String value;
  private String alt;
  private String border;
  private String tip;
  private String width;
  private String height;

  @Override
  public String getComponentType() {
    return UIGraphic.COMPONENT_TYPE;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_ALT, alt);
    ComponentUtil.setStringProperty(component, ATTR_BORDER, border);
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width);
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height);
  }

  @Override
  public void release() {
    super.release();
    this.alt = null;
    this.border = null;
    this.value = null;
    this.tip = null;
    width = null;
    height = null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getAlt() {
    return alt;
  }

  public void setAlt(String alt) {
    this.alt = alt;
  }

  public String getBorder() {
    return border;
  }

  public void setBorder(String border) {
    this.border = border;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }
}
