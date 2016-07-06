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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.TextAlign;

/**
 * Renders a FlowLayout that positions the content components in there natural order.
 */
@Tag(name = "flowLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFlowLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = AbstractUIFlowLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.FLOW_LAYOUT,
    allowedChildComponenents = "NONE")
public interface FlowLayoutTagDeclaration
    extends HasIdBindingAndRendered, IsVisual {

  /**
   * The alignment of the elements inside of the container, possible values are:
   * {@link TextAlign#left},
   * {@link TextAlign#right},
   * {@link TextAlign#center} and
   * {@link TextAlign#justify}.
   *
   * @since 3.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"org.apache.myfaces.tobago.layout.TextAlign"},
      allowedValues = {
          TextAlign.LEFT, TextAlign.RIGHT, TextAlign.CENTER, TextAlign.JUSTIFY
      })
  void setTextAlign(String textAlign);

}
