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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.taglib.decl.HasDeprecatedDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;

/**
 * Renders a panel with border and title.
 */
@Tag(name = "box")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIBox",
    rendererType = "Box",
    facets = {
    @Facet(name = "toolBar", description = "Contains an instance of UIToolBar"),
    @Facet(name = "layout", description = "Contains an instance of UILayout")})

public interface BoxTagDeclaration extends TobagoBodyTagDeclaration,
    HasIdBindingAndRendered, HasDeprecatedDimension, HasLabel {

  /**
   * Indicate markup of this component.
   * Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none")
  void setMarkup(String markup);
}
