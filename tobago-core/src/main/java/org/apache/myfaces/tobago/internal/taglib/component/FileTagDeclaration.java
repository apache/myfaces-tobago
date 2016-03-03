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

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UIInput;

/**
 * Renders a file input field.
 * <p />
 * For content constraints please use <a href="validateFileItem.html">tc:validateFileItem</a>.
 */
@Tag(name = "file")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFile",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIFile",
    uiComponentFacesClass = "javax.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.FILE,
    allowedChildComponenents = "NONE")
public interface FileTagDeclaration
    extends HasValidator, HasValidatorMessage, HasRequiredMessage, HasConverterMessage,
    HasValueChangeListener, HasIdBindingAndRendered, IsDisabled, IsFocus,
    HasLabel, HasLabelLayout, HasTip, IsReadonly, IsRequired, HasTabIndex, IsVisual {

  /**
   * Value binding expression pointing to a
   * {@link javax.servlet.http.Part} property to store the
   * uploaded file.
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = { "javax.servlet.http.Part" },
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);
}
