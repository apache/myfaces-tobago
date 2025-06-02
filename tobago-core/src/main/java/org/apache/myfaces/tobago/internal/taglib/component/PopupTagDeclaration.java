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

import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.model.CollapseMode;

/**
 * Renders a popup panel.
 */
@Tag(name = "popup")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIPopup",
    uiComponentFacesClass = "jakarta.faces.component.UIPanel",
    componentFamily = AbstractUIPopup.COMPONENT_FAMILY,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    rendererType = RendererTypes.POPUP,
    facets = {
        @Facet(name = Facets.BAR, description = "Code is placed in the header at the bar position."),
        @Facet(name = Facets.LABEL, description = "Code is placed in the header at the label position."),
        @Facet(name = Facets.FOOTER, description = "Code is placed in the footer.")
    },
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_EXTRA_LARGE,
            description = "Extra large popup"
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LARGE,
            description = "Large popup"
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SMALL,
            description = "Small popup"
        )
    })
public interface PopupTagDeclaration
    extends HasIdBindingAndRendered, IsVisual, HasTip {

  /**
   * If "true", a click on the backdrop does not close the popup.
   * Default value is "false" and can be configured in the tobago-config.xml.
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = "boolean",
      defaultCode = "Boolean.parseBoolean(org.apache.myfaces.tobago.context."
          + "TobagoContext.getInstance(getFacesContext()).getTheme().getTagAttributeDefault(TAG, \"modal\"))")
  void setModal(String modal);

  /**
   * Indicating the collapsed state of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setCollapsed(String collapsed);

  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.CollapseMode",
      defaultValue = CollapseMode.ABSENT,
      defaultCode = "org.apache.myfaces.tobago.model.CollapseMode.absent")
  void setCollapsedMode(String collapsed);
}
