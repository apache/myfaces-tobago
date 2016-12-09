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

import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UIForm;

/**
 * <p>
 * Renders a page element.
 * </p>
 * <p>
 * The markup {@link Markup#SPREAD} means the page should spread over the hole available area.
 * So the content will use the full height of the browser window.
 * <b>Warning: This feature is preliminary and may change, if necessary, in minor releases!</b>
 * </p>
 */
@Tag(name = "page")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIPage",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIPage",
    uiComponentFacesClass = "javax.faces.component.UIForm",
    componentFamily = UIForm.COMPONENT_FAMILY,
    rendererType = RendererTypes.PAGE,
    facets =
        { @Facet(name=Facets.LAYOUT, description = "Deprecated! Contains an layout manager. "
              + "The layout manager tag should surround the content instead.")},
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CLICK,
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.DBLCLICK),
        @Behavior(
            name = ClientBehaviors.LOAD),
        @Behavior(
            name = ClientBehaviors.RESIZE)
    })

public interface PageTagDeclaration
    extends HasLabel, HasId, HasBinding, IsVisual {

  /**
   * Contains the id of the component which should have the focus after
   * loading the page.
   * Set to empty string for disabling setting of focus.
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
}
