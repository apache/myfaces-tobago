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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DEFAULT_COMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILinkCommand;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;

public class LinkTag extends AbstractCommandTag
    implements LinkTagDeclaration {

  private static final Log LOG = LogFactory.getLog(LinkTag.class);

  private String target;
  private String label;
  private String image;
  private String tip;
  private String defaultCommand;
  private String markup;
  private String tabIndex;

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_TARGET, target);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setBooleanProperty(component, ATTR_DEFAULT_COMMAND, defaultCommand);
    ComponentUtil.setMarkup(component, markup);
    ComponentUtil.setIntegerProperty(component, ATTR_TAB_INDEX, tabIndex);
  }

  public String getComponentType() {
    return UILinkCommand.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    target = null;
    label = null;
    image = null;
    tip = null;
    defaultCommand = null;
    markup = null;
    tabIndex = null;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setDefaultCommand(String defaultCommand) {
    this.defaultCommand = defaultCommand;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }
}
