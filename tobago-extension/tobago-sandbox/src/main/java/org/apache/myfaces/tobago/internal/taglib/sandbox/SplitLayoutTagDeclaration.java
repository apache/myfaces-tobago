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

package org.apache.myfaces.tobago.internal.taglib.sandbox;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.Orientation;

/**
   * Renders a SplitLayout.
   * A area with two child components rendered horizontally or vertically and allows to change the
   * layout relation of this two components on the client.
   */
@Tag(name = "splitLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISplitLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISplitLayout",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = AbstractUIGridLayout.COMPONENT_FAMILY,
    rendererType = "SplitLayout",
    allowedChildComponenents = "NONE")
public interface SplitLayoutTagDeclaration
      extends HasId, HasSpacing, HasBinding, IsVisual {

  /**
   * This value defines the layout constraints for the layout.
   * It is two layout tokens separated by a semicolon. See GridLayout.
   * Example: '2*;*'.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "1*;1*")
  void setLayout(String rows);

  /**
   * This value defines the orientation of the split layout.
   * Possible values are {@link Orientation#horizontal} and {@link Orientation#vertical}.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Orientation",
      allowedValues = {Orientation.HORIZONTAL, Orientation.VERTICAL})
  void setOrientation(String orientation);

  /**
   * This attribute advises the layout manager, to not use space that comes from non rendered components.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean")
  void setRigid(String rigid);
}
