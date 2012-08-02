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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import org.apache.myfaces.tobago.component.UIToolBar;

import javax.faces.component.UIComponent;


public class BoxToolBarRenderer extends ToolBarRendererBase {

  @Override
  protected String getLabelPosition(UIComponent component) {
    final String attribute = (String) component.getAttributes().get(ATTR_LABEL_POSITION);
    if (UIToolBar.LABEL_BOTTOM.equals(attribute)) {
      return UIToolBar.LABEL_RIGHT;
    } else {
      return attribute;
    }
  }

  @Override
  protected String getIconSize(UIComponent component) {
    final String attribute = (String) component.getAttributes().get(ATTR_ICON_SIZE);
    if (UIToolBar.ICON_BIG.equals(attribute)) {
      return UIToolBar.ICON_SMALL;
    } else {
      return attribute;
    }
  }

  @Override
  protected String getHoverClasses(boolean first, boolean last) {
    return "tobago-toolBar-button-hover tobago-toolBar-button-box-facet-hover"
        + (last ? " tobago-box-toolBar-button-hover-last" : "");
  }

  @Override
  protected String getTableClasses(boolean selected, boolean disabled) {
    return
        "tobago-toolbar-button-table tobago-boxToolbar-button-table-box-facet tobago-toolbar-button-table-box-facet-"
            + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");
  }

  @Override
  protected String getDivClasses(boolean selected, boolean disabled) {
    return "tobago-toolbar-button tobago-toolbar-button-box-facet tobago-toolbar-button-box-facet-"
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");
  }
}
