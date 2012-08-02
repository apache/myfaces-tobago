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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;

public class LabelTag extends BeanTag implements LabelTagDeclaration {
  private static final Log LOG = LogFactory.getLog(LabelTag.class);

  private String forComponent;
  private String tip;
  private String markup;


  @Override
  public String getComponentType() {
    return UILabel.COMPONENT_TYPE;
  }

  public String getFor() {
    return forComponent;
  }

  @Override
  public void release() {
    super.release();
    tip = null;
    forComponent = null;
    markup = null;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setMarkup(component, markup);
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

  public String getMarkup() {
    return markup;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }
}

