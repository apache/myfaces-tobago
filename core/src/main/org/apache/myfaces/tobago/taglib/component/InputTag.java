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
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public abstract class InputTag extends BeanTag implements org.apache.myfaces.tobago.taglib.decl.InputTag {
// ----------------------------------------------------------------- attributes

  private String onchange;
  private String focus;
  private String accessKey;
  private String labelWithAccessKey;
  private String tip;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    this.onchange = null;
    this.focus = null;
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;

  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component,
        ATTR_ONCHANGE, onchange, getIterationHelper());
    ComponentUtil.setBooleanProperty(component,
        ATTR_FOCUS, focus, getIterationHelper());
    ComponentUtil.setStringProperty(component,
        ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component,
        ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component,
        ATTR_TIP, tip, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getOnchange() {
    return onchange;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public String getFocus() {
    return focus;
  }
  public void setFocus(String focus) {
    this.focus = focus;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}

