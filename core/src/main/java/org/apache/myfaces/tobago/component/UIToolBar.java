package org.apache.myfaces.tobago.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;

import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
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

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.ToolBar";

  private String labelPosition;
  private String iconSize;

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

  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[3];
    saveState[0] = super.saveState(context);
    saveState[1] = labelPosition;
    saveState[2] = iconSize;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    labelPosition = (String) values[1];
    iconSize = (String) values[2];
  }
}
