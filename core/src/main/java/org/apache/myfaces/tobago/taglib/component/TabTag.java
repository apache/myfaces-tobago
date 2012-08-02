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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.tagext.BodyTag;

// Some Weblogic versions need explicit 'implements' for BodyTag
public class TabTag extends TobagoBodyTag
    implements BodyTag, TabTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TabTag.class);

  private String label;
  private String tip;
  private String markup;
  private String disabled;

  public String getComponentType() {
    return UITab.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setMarkup(component, markup);
    ComponentUtil.setBooleanProperty(component, ATTR_DISABLED, disabled);
  }

  public void release() {
    super.release();
    label = null;
    tip = null;
    markup = null;
    disabled = null;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
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
    return label;
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

  public String getDisabled() {
    return disabled;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }
}

