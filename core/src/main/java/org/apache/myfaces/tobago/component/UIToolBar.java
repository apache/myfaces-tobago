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

package org.apache.myfaces.tobago.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ORIENTATION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;

import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/*
 * Date: 11.02.2006
 * Time: 14:48:46
 */
public class UIToolBar extends javax.faces.component.UIPanel {

  public static final String LABEL_BOTTOM = "bottom";
  public static final String LABEL_RIGHT = "right";
  public static final String LABEL_OFF = "off";

  public static final String ICON_SMALL = "small";
  public static final String ICON_BIG = "big";
  public static final String ICON_OFF = "off";

  public static final String ORIENTATION_LEFT = "left";
  public static final String ORIENTATION_RIGHT = "right";

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.ToolBar";

  private String labelPosition;
  private String iconSize;
  private String orientation;

  public String getLabelPosition() {
    if (labelPosition != null) {
      return labelPosition;
    }
    ValueBinding vb = getValueBinding(ATTR_LABEL_POSITION);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return LABEL_BOTTOM;
    }
  }

  public void setLabelPosition(String labelPosition) {
    this.labelPosition = labelPosition;
  }

  public String getIconSize() {
    if (iconSize != null) {
      return iconSize;
    }
    ValueBinding vb = getValueBinding(ATTR_ICON_SIZE);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return ICON_SMALL;
    }
  }

  public void setIconSize(String iconSize) {
    this.iconSize = iconSize;
  }


  public String getOrientation() {
    if (orientation != null) {
      return orientation;
    }
    ValueBinding vb = getValueBinding(ATTR_ORIENTATION);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return ORIENTATION_LEFT;
    }

  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
  }

  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[4];
    saveState[0] = super.saveState(context);
    saveState[1] = labelPosition;
    saveState[2] = iconSize;
    saveState[3] = orientation;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    labelPosition = (String) values[1];
    iconSize = (String) values[2];
    orientation = (String) values[3];
  }
}
