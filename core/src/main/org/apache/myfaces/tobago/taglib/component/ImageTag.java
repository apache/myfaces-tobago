/*
 * Copyright 2002-2005 atanion GmbH.
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
 * User: weber
 * Date: Jun 3, 2002
 * Time: 3:10:17 PM
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;

public class ImageTag extends TobagoTag
    implements org.apache.myfaces.tobago.taglib.decl.ImageTag {

  private String value;
  private String alt;
  private String border;
  private String tip;

  public String getComponentType() {
    return UIGraphic.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_ALT, alt);
    ComponentUtil.setStringProperty(component, ATTR_BORDER, border);
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
  }

  public void release() {
    super.release();
    this.alt = null;
    this.border = null;
    this.value = null;
    this.tip = null;
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
}
