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

import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIFlexLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.AlignItems;
import org.apache.myfaces.tobago.layout.JustifyContent;

/**
 * Renders a &lt;a href=https://www.w3.org/TR/css-flexbox-1/&gt;Flexible Box Layout&lt;/a&gt;.
 *
 * @since 3.0.0
 */
@Tag(name = "flexLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFlexLayout",
    componentFamily = AbstractUIFlexLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.FLEX_LAYOUT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "ALL",
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SPREAD,
            description = "Use the full height for the HTML content."
        )
    })
public interface FlexLayoutTagDeclaration extends HasIdBindingAndRendered, IsVisual {

  /**
   * This value defines the layout constraints for column layout.
   * It is a space separated list of layout tokens '&lt;n&gt;fr', '&lt;measure&gt;' or the keyword 'auto'.
   * Where &lt;n&gt; is a positive integer and &lt;measure&gt; is a valid CSS length.
   * Example: '2fr 1fr 100px 3rem auto'.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.MeasureList")
  void setColumns(String columns);

  /**
   * This value defines the layout constraints for row layout.
   * It is a space separated list of layout tokens '&lt;n&gt;fr', '&lt;measure&gt;' or the keyword 'auto'.
   * Where &lt;n&gt; is a positive integer and &lt;measure&gt; is a valid CSS length.
   * Example: '2fr 1fr 100px 3rem auto'.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.MeasureList")
  void setRows(String rows);

  /**
   * This value defines CSS align-items value of the flex layout.
   *
   * @since 3.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.AlignItems",
      allowedValues = {
          AlignItems.FLEX_START, AlignItems.FLEX_END, AlignItems.BASELINE, AlignItems.CENTER, AlignItems.STRETCH
      })
  void setAlignItems(String alignItems);

  /**
   * This value defines CSS justify-content value of the flex layout.
   *
   * @since 3.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.JustifyContent",
      allowedValues = {
          JustifyContent.FLEX_START, JustifyContent.FLEX_END, JustifyContent.CENTER, JustifyContent.SPACE_BETWEEN,
          JustifyContent.SPACE_AROUND
      })
  void setJustifyContent(String justifyContent);

}
