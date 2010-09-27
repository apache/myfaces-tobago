package org.apache.myfaces.tobago.internal.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasState;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponentWithDeprecatedDimension;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutContainer;

/**
 * Renders a page element.
 */
@Tag(name = "page")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIPage",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIPage",
    rendererType = RendererTypes.PAGE,
    facets =
        { @Facet(name = Facets.ACTION,
                description ="Contains an instance of UICommand (tc:command) for an auto-action",
                allowedChildComponenents = "org.apache.myfaces.tobago.Command"),
          @Facet(name = Facets.RESIZE_ACTION,
                description ="Contains an instance of UICommand (tc:command) or a UIForm (tc:form) with a UICommand"
                    + " for a resize-action",
                allowedChildComponenents = {"org.apache.myfaces.tobago.Command", "org.apache.myfaces.tobago.Form"}),
          @Facet(name = Facets.MENUBAR, description = "Menubar",
                allowedChildComponenents = "javax.faces.component.UIPanel"), //fake
          @Facet(name=Facets.LAYOUT, description = "Contains an instance of AbstractUILayoutBase",
                allowedChildComponenents = "org.apache.myfaces.tobago.GridLayout")})

public interface PageTagDeclaration
    extends HasLabel, HasId, HasBinding, HasState, HasMarkup, HasCurrentMarkup,
    IsGridLayoutComponentWithDeprecatedDimension, IsGridLayoutContainer {

  /**
   * Contains the id of the component wich should have the focus after
   * loading the page.
   * Set to emtpy string for disabling setting of focus.
   * Default (null) enables the "auto focus" feature.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setFocusId(String focusId);

  /**
   * Absolute URL to an image or image name to lookup in tobago resource path
   * representing the application. In HTML it is used as a favicon.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setApplicationIcon(String icon);

  // XXX LAYOUT_GENERATE
//  @TagAttribute
//  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
//  void setHeight(String height);

  // XXX LAYOUT_GENERATE
//  @TagAttribute
//  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
//  void setWidth(String width);

}
