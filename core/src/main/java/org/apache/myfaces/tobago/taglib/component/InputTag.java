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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOCUS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCHANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

public abstract class InputTag extends BeanTag implements InputTagDeclaration {
  private static final Log LOG = LogFactory.getLog(InputTag.class);

  private String onchange;
  private String focus;
  private String tip;
  private String validator;
  private String valueChangeListener;
  private String tabIndex;

  public void release() {
    super.release();
    this.onchange = null;
    this.focus = null;
    tip = null;
    validator = null;
    valueChangeListener = null;
    tabIndex = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_ONCHANGE, onchange);
    ComponentUtil.setBooleanProperty(component, ATTR_FOCUS, focus);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setIntegerProperty(component, ATTR_TAB_INDEX, tabIndex);
    if (component instanceof EditableValueHolder) {
      EditableValueHolder editableValueHolder = (EditableValueHolder) component;
      ComponentUtil.setValidator(editableValueHolder, validator);
      ComponentUtil.setValueChangeListener(editableValueHolder, valueChangeListener);
    }
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
    return null;
  }

  public void setAccessKey(String accessKey) {
    if (Deprecation.LOG.isErrorEnabled()) {
      Deprecation.LOG.error("Attribute 'accessKey' doesn't work any longer "
          + "and will removed soon! Please use special syntax of 'label' instead.");
    }
  }

  public String getLabelWithAccessKey() {
    return null;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn("Attribute 'labelWithAccessKey' is deprecated, "
          + "and will removed soon! Please use 'label' instead.");
    }
    setLabel(labelWithAccessKey);
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

  public String getValueChangeListener() {
    return valueChangeListener;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  public String getTabIndex() {
    return tabIndex;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }
}

