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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUCOMMAND;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;


public class MenuCommandTag extends AbstractCommandTag
    implements MenuCommandTagDeclaration {
  private static final Log LOG = LogFactory.getLog(MenuCommandTag.class);

  //public static final String COMMAND_TYPE = "menuCommand";

  private String image;
  private String label;
  private String target;

  public String getComponentType() {
    return UIMenuCommand.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    image = null;
    label = null;
    target = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image);
    //ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, COMMAND_TYPE);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_TARGET, target);
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public void setTarget(String target) {
    this.target = target;
  }
}
