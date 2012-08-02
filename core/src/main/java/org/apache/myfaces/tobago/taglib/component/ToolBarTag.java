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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ORIENTATION;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIToolBar;

import javax.faces.component.UIComponent;

/*
 * Created 29.07.2003 at 15:09:53.
 * $Id$
*/
public class ToolBarTag extends PanelTag
    implements ToolBarTagDeclaration {

  public static final String LABEL_BOTTOM = UIToolBar.LABEL_BOTTOM;
  public static final String LABEL_RIGHT = UIToolBar.LABEL_RIGHT;
  public static final String LABEL_OFF = UIToolBar.LABEL_OFF;

  public static final String ICON_SMALL = UIToolBar.ICON_SMALL;
  public static final String ICON_BIG = UIToolBar.ICON_BIG;
  public static final String ICON_OFF = UIToolBar.ICON_OFF;

  private String labelPosition = LABEL_BOTTOM;
  private String iconSize = ICON_SMALL;
  private String orientation = UIToolBar.ORIENTATION_LEFT;


  public String getComponentType() {
    return UIToolBar.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_LABEL_POSITION, labelPosition);
    ComponentUtil.setStringProperty(component, ATTR_ICON_SIZE, iconSize);
    ComponentUtil.setStringProperty(component, ATTR_ORIENTATION, orientation);
  }

  public void release() {
    super.release();
    labelPosition = LABEL_BOTTOM;
    iconSize = ICON_SMALL;
  }

  public void setLabelPosition(String labelPosition) {
    this.labelPosition = labelPosition;
  }

  public void setIconSize(String iconSize) {
    this.iconSize = iconSize;
  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
  }
}
