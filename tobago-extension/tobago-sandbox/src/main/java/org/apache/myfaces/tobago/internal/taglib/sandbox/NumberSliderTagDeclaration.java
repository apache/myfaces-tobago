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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasStyle;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;

import javax.faces.component.UIInput;

/**
 * Renders a slider to select a number in a range.
 */
@Tag(name = "numberSlider")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UINumberSlider",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUINumberSlider",
    uiComponentFacesClass = "javax.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = "NumberSlider")
public interface NumberSliderTagDeclaration
    extends HasIdBindingAndRendered, IsReadonly, IsDisabled, HasMarkup, HasCurrentMarkup,
    HasValue, HasValueChangeListener, HasStyle {

  /**
   * The minimum integer that can be entered and which represents the left
   * border of the slider.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setMin(String min);

  /**
   * The maximum integer that can be entered and which represents the right
   * border of the slider.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "100")
  void setMax(String max);
}
