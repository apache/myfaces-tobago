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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LEFT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TOP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPopup;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.tagext.BodyTag;

public class PopupTag extends TobagoBodyTag
    implements BodyTag, PopupTagDeclaration {
  private String width;
  private String height;
  private String left;
  private String top;

  public String getComponentType() {
    return UIPopup.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    width = null;
    height = null;
    left = null;
    top = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width);
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height);
    ComponentUtil.setStringProperty(component, ATTR_LEFT, left);
    ComponentUtil.setStringProperty(component, ATTR_TOP, top);
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public void setTop(String top) {
    this.top = top;
  }

}

