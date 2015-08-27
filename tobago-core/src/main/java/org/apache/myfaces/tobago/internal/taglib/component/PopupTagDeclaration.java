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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponentWithDimension;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutContainer;

import javax.faces.component.UIPanel;

/**
 * Renders a popup panel.
 * The popup gets a grid layout manager with columns="auto" and rows="auto" as definition.
 * So a popup should contain only one layout component.
 * The default layout manager can be overwritten with the layout facet.
 */
@Tag(name = "popup")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIPopup",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIPopup",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.POPUP,
    facets = {
        @Facet(name = Facets.LAYOUT,
            description = "Contains an instance of AbstractUILayoutBase",
            allowedChildComponenents = "org.apache.myfaces.tobago.GridLayout")}
)
public interface PopupTagDeclaration 
    extends HasIdBindingAndRendered, IsGridLayoutComponentWithDimension, IsGridLayoutContainer,
    HasMarkup, HasCurrentMarkup {

  /**
   * The rest of the page will be faded off for the time the popup is displayed.
   * <br>
   * XXX Currently (since 3.0 branch) non-modal is not supported.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setModal(String modal);
  
  /**
   * This value will usually be set by the layout manager.
   *
   * @param left The left position of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setLeft(String left);

  /**
   * This value will usually be set by the layout manager.
   *
   * @param top The top position of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setTop(String top);

  /**
   * TBD: Remove this attribute?
   *
   * Represents the visibility order of the popup. The renderer may implemented this visibility by the CSS z-index.
   *
   * @param zIndex The visibility order.
   */
  @UIComponentTagAttribute(type = "java.lang.Integer")
  void setZIndex(String zIndex);

}
