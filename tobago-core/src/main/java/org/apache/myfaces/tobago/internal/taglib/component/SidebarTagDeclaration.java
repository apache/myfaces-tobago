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

package org.apache.myfaces.tobago.internal.taglib.component;

import jakarta.faces.component.UIPanel;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.SidebarPlacement;
import org.apache.myfaces.tobago.model.CollapseMode;

/**
 * Sidebar component for hidden content that slides into the page.
 */
@Tag(name = "sidebar")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISidebar",
    uiComponentFacesClass = "jakarta.faces.component.UIPanel",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    rendererType = RendererTypes.SIDEBAR,
    facets = {
        @Facet(name = Facets.BAR, description = "Code is placed in the header at the bar position."),
        @Facet(name = Facets.LABEL, description = "Code is placed in the header at the label position.")
    })
public interface SidebarTagDeclaration extends HasIdBindingAndRendered, IsVisual, HasTip {

  /**
   * Indicating the collapsed state of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setCollapsed(String collapsed);

  /**
   * Enum indicating the mode of the collapsed state of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.CollapseMode",
      defaultValue = CollapseMode.ABSENT,
      defaultCode = "org.apache.myfaces.tobago.model.CollapseMode.absent")
  void setCollapsedMode(String collapsed);

  /**
   * Defines the placement of the sidebar. Default is "left".
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.SidebarPlacement",
      allowedValues = {
          SidebarPlacement.TOP,
          SidebarPlacement.LEFT,
          SidebarPlacement.RIGHT,
          SidebarPlacement.BOTTOM
      },
      defaultValue = SidebarPlacement.LEFT,
      defaultCode = "org.apache.myfaces.tobago.layout.SidebarPlacement.left")
  void setPlacement(String placement);
}
