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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutoSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasHelp;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasPlaceholder;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.model.DateType;

import jakarta.faces.component.UIInput;

/**
 * Renders a date input field.
 * <p>
 * For a time input field set you'll need to set the &lt;f:convertDateTime type="time"&gt; inside the &lt;tc:date&gt;.
 * <p>
 * If there is no converter given, a default instance of {@link jakarta.faces.convert.DateTimeConverter} will be used.
 */
@Tag(name = "date")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIDate",
    uiComponentFacesClass = "jakarta.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.DATE,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(name = Facets.BEFORE,
            description =
                "This facet can contain a part for input groups."),
        @Facet(name = Facets.AFTER,
            description =
                "This facet can contain a part for input groups.")
    },
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CHANGE,
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.INPUT),
        @Behavior(
            name = ClientBehaviors.CLICK),
        @Behavior(
            name = ClientBehaviors.DBLCLICK),
        @Behavior(
            name = ClientBehaviors.FOCUS),
        @Behavior(
            name = ClientBehaviors.BLUR)
    })
public interface DateTagDeclaration
    extends HasAccessKey, HasValidator, HasValue, HasValueChangeListener, HasTabIndex, IsFocus, IsVisual,
    HasValidatorMessage, HasConverterMessage, HasRequiredMessage, HasIdBindingAndRendered, IsReadonly,
    IsDisabled, HasConverter, HasLabel, HasHelp, HasLabelLayout,
    HasTip, IsRequired, HasPlaceholder, HasAutoSpacing {

  /**
   * If true, a today- or now-button is displayed.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setTodayButton(String required);

  /**
   * Sets the minimum value of the date.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {
      "java.time.LocalDate",
      "java.time.LocalTime",
      "java.time.LocalDateTime",
      "java.time.ZonedDateTime",
      "java.time.Month",
      "java.util.Date",
      "java.lang.String"
  })
  void setValue(String value);

  /**
   * Sets the minimum value of the date.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {
      "java.time.LocalDate",
      "java.time.LocalTime",
      "java.time.LocalDateTime",
      "java.time.ZonedDateTime",
      "java.time.Month",
      "java.util.Date",
      "java.lang.String"
  })
  void setMin(String min);

  /**
   * Sets the maximum value of the date.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {
      "java.time.LocalDate",
      "java.time.LocalTime",
      "java.time.LocalDateTime",
      "java.time.ZonedDateTime",
      "java.time.Month",
      "java.util.Date",
      "java.lang.String"
  })
  void setMax(String max);

  /**
   * Sets the step of a date or time picker. Value is in seconds.
   * Reasonable values are e.g. 0.001, 1, 5, 10, 60, 300, 3600.
   * If not set, browsers drop the seconds, so it's the same as the value of 60.
   * To show seconds use 1.
   * To show milliseconds use 0.001.
   * Browser support is in progress.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Double"})
  void setStep(String step);

  /**
   * Type of the date/time input.
   *
   * Warning: month and week currently not supported.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.DateType",
      allowedValues = {
          DateType.STRING_DATE,
          DateType.STRING_TIME,
          DateType.STRING_DATETIME_LOCAL,
          DateType.STRING_MONTH,
          DateType.STRING_WEEK
      })
  void setType(String type);
}
