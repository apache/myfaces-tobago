/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasAction;
import org.apache.myfaces.tobago.taglib.decl.HasActionListener;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasImage;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;
import org.apache.myfaces.tobago.taglib.decl.IsDefaultCommand;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;
import org.apache.myfaces.tobago.taglib.decl.IsInline;

import javax.faces.component.UIComponent;

/**
 * Renders a button element.
 */
// fixme: bodyContent
@Tag(name = "button")
@BodyContentDescription(anyTagOf = "facestag")
public class ButtonTag extends CommandTag
    implements HasIdBindingAndRendered, HasLabelAndAccessKey, HasImage,
    IsDisabled, HasAction, HasActionListener, HasCommandType,
    IsImmediateCommand, IsDefaultCommand,
    HasWidth, HasTip, IsInline {

  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;
  private String tip;
  private String defaultCommand;


  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image);
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey);
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
    ComponentUtil.setBooleanProperty(component, ATTR_DEFAULT_COMMAND, defaultCommand);
  }

  public void release() {
    super.release();
    label = null;
    image = null;
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
    defaultCommand = null;
  }


  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
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

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setDefaultCommand(String defaultCommand) {
    this.defaultCommand = defaultCommand;
  }
}

