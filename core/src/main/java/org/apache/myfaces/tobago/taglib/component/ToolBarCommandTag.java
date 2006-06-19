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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BUTTON;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;


public class ToolBarCommandTag extends CommandTag
    implements ToolBarCommandTagDeclaration {

  private static final Log LOG = LogFactory.getLog(ToolBarCommandTag.class);

  private String label;
  private String image;
  private String tip;

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(RENDERER_TYPE_BUTTON);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
  }

  public void release() {
    super.release();
    label = null;
    image = null;
    tip = null;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public String getAccessKey() {
    return null;
  }

  public void setAccessKey(String accessKey) {
    LOG.warn("Attibute 'accessKey' is deprecated, "
        + "and will removed soon!");
  }

  public String getLabelWithAccessKey() {
    return null;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    LOG.warn("Attibute 'labelWithAccessKey' is deprecated, "
        + "and will removed soon! Please use 'label' instead.");
    setLabel(labelWithAccessKey);
  }
}

