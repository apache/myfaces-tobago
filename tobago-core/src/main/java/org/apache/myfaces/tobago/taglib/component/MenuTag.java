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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COMMAND_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_WITH_ACCESS_KEY;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasImage;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;

/**
 * Container component to hold submenus and items.
 */
@Tag(name = "menu")
@BodyContentDescription(
    anyClassOf = { "org.apache.myfaces.tobago.taglib.component.MenuTag",
        "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag" })

public class MenuTag extends TobagoTag
    implements HasIdBindingAndRendered, HasLabelAndAccessKey, IsDisabled, HasImage {

  public static final String MENU_TYPE = "menu";

  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;
//  private String disabled;

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(null);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey);
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image);
    ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, "menu");
  }

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }


  public void release() {
    super.release();
    label = null;
    accessKey = null;
    labelWithAccessKey = null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
