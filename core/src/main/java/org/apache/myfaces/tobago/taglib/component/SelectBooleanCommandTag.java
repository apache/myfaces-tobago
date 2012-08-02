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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUCOMMAND;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;

/**
 * User: weber
 * Date: Apr 26, 2005
 * Time: 3:01:45 PM
 */
public class SelectBooleanCommandTag extends AbstractCommandTag {

  private static final Log LOG = LogFactory.getLog(SelectBooleanCommandTag.class);

  private String label;
  private String value;
  private String tip;


  public String getComponentType() {
    return UISelectBooleanCommand.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
  }

  public void release() {
    super.release();
    value = null;
    label = null;
    tip = null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
}
