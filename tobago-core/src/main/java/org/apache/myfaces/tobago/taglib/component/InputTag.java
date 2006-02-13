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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACCESS_KEY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOCUS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_WITH_ACCESS_KEY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCHANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public abstract class InputTag extends BeanTag implements InputTagDeclaration {

  private String onchange;
  private String focus;
  private String accessKey;
  private String labelWithAccessKey;
  private String tip;
  private String validator;

  public void release() {
    super.release();
    this.onchange = null;
    this.focus = null;
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
    validator = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_ONCHANGE, onchange);
    ComponentUtil.setBooleanProperty(component, ATTR_FOCUS, focus);
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey);
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setValidator(component, validator);
  }

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

  public String getValidator() {
    return validator;
  }

  public void setValidator(String validator) {
    this.validator = validator;
  }
}

