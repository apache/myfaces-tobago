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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMargin;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMargins;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.layout.TextAlign;

/**
 * XXX Warning: Still in progress! Please do not use it until other announcement.
 * Renders a FlowLayout that positions the content components in there natural order.
 */
@Tag(name = "flowLayout", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFlowLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = AbstractUIFlowLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.FLOW_LAYOUT,
    allowedChildComponenents = "NONE", isLayout = true)
public interface FlowLayoutTagDeclaration
    extends HasId,
    /*HasSpacing, TODO*/
    HasMargin, HasMargins, /*todo: do we need the margin here? Or should be use the margin from the container? */ 
    HasBinding, HasMarkup, HasCurrentMarkup {

  /**
   * The alignment of the elements inside of the container, possible values are:
   * {@value TextAlign#STRING_LEFT},
   * {@value TextAlign#STRING_RIGHT},
   * {@value TextAlign#STRING_CENTER} und
   * {@value TextAlign#STRING_JUSTIFY}.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"org.apache.myfaces.tobago.layout.TextAlign"},
      defaultValue = "TextAlign.LEFT",
      allowedValues = {
          TextAlign.STRING_LEFT, TextAlign.STRING_RIGHT, TextAlign.STRING_CENTER, TextAlign.STRING_JUSTIFY})
  void setTextAlign(String textAlign);
}
