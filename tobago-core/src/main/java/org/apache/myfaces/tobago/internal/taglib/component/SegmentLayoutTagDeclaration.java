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
import org.apache.myfaces.tobago.internal.component.AbstractUISegmentLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.SegmentJustify;

/**
 * Renders a layout using a 12 columns grid.
 * Find more information on how the grid works in the Twitter Bootstrap documentation.
 *
 * If no attribute is defined, extraSmall="12seg" will be used as default.
 *
 * @since 3.0.0
 */
@Tag(name = "segmentLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISegmentLayout",
    componentFamily = AbstractUISegmentLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.SEGMENT_LAYOUT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "NONE",
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SPREAD,
            description = "Use the full height for the HTML content."
        )
    })
public interface SegmentLayoutTagDeclaration extends HasIdBindingAndRendered, IsVisual {

  /**
   * The space separated definition of the columns for extra small devices.
   * Possible values are: [1-12]seg, 'auto' and &lt;n&gt;fr. Where &lt;n&gt; is a positive integer.
   * Example: '1seg 5seg 1fr auto'
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.SegmentMeasureList")
  void setExtraSmall(String extraSmall);

  /**
   * The space separated definition of the columns for small devices.
   * Possible values are: [1-12]seg, 'auto' and &lt;n&gt;fr. Where &lt;n&gt; is a positive integer.
   * Example: '1seg 5seg 1fr auto'
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.SegmentMeasureList")
  void setSmall(String small);

  /**
   * The space separated definition of the columns for medium devices.
   * Possible values are: [1-12]seg, 'auto' and &lt;n&gt;fr. Where &lt;n&gt; is a positive integer.
   * Example: '1seg 5seg fr auto'
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.SegmentMeasureList")
  void setMedium(String medium);

  /**
   * The space separated definition of the columns for large devices.
   * Possible values are: [1-12]seg, 'auto' and &lt;n&gt;fr. Where &lt;n&gt; is a positive integer.
   * Example: '1seg 5seg 1fr auto'
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.SegmentMeasureList")
  void setLarge(String large);

  /**
   * The space separated definition of the columns for extra large devices.
   * Possible values are: [1-12]seg, 'auto' and &lt;n&gt;fr. Where &lt;n&gt; is a positive integer.
   * Example: '1seg 5seg 1fr auto'
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.SegmentMeasureList")
  void setExtraLarge(String extraLarge);

  /**
   * The space separated definition of the columns for extra extra large devices.
   * Possible values are: [1-12]seg, 'auto' and &lt;n&gt;fr. Where &lt;n&gt; is a positive integer.
   * Example: '1seg 5seg 1fr auto'
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.SegmentMeasureList")
  void setExtra2Large(String extraLarge);

  /**
   * The space separated definition of the column margins for extra small devices.
   * Allowed values are: none, left, right, both
   * Example: 'left none both'
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setMarginExtraSmall(String marginExtraSmall);

  /**
   * The space separated definition of the column margins for small devices.
   * Allowed values are: none, left, right, both
   * Example: 'left none both'
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setMarginSmall(String marginSmall);

  /**
   * The space separated definition of the column margins for medium devices.
   * Allowed values are: none, left, right, both
   * Example: 'left none both'
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setMarginMedium(String marginMedium);

  /**
   * The space separated definition of the column margins for large devices.
   * Allowed values are: none, left, right, both
   * Example: 'left none both'
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setMarginLarge(String marginLarge);

  /**
   * The space separated definition of the column margins for extra large devices.
   * Allowed values are: none, left, right, both
   * Example: 'left none both'
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setMarginExtraLarge(String marginExtraLarge);

  /**
   * The space separated definition of the column margins for extra extra large devices.
   * Allowed values are: none, left, right, both
   * Example: 'left none both'
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setMarginExtra2Large(String marginExtra2Large);

  /**
   * The horizontal alignment of the elements inside of the grid, possible values are:
   * {@link SegmentJustify#NONE},
   * {@link SegmentJustify#START},
   * {@link SegmentJustify#CENTER},
   * {@link SegmentJustify#END},
   * {@link SegmentJustify#AROUND} and
   * {@link SegmentJustify#BETWEEN}.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.SegmentJustify",
      allowedValues = {
          SegmentJustify.NONE, SegmentJustify.START, SegmentJustify.CENTER,
          SegmentJustify.END, SegmentJustify.AROUND, SegmentJustify.BETWEEN
      })
  void setJustify(String justify);
}
