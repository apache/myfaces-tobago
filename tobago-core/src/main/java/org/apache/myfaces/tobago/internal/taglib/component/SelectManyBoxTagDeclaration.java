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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOnchange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasPlaceholder;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessageForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequiredForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.Select2;

import javax.faces.component.UISelectMany;

/**
 * Render a multi selection option listbox.
 */
@Tag(name = "selectManyBox")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectManyBox",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectManyBox",
    uiComponentFacesClass = "javax.faces.component.UISelectMany",
    componentFamily = UISelectMany.COMPONENT_FAMILY,
    rendererType = RendererTypes.SELECT_MANY_BOX,
    allowedChildComponenents = {"javax.faces.SelectItem", "javax.faces.SelectItems"})

public interface SelectManyBoxTagDeclaration
    extends HasId, IsDisabled, IsRendered, HasBinding, HasTip,
    IsReadonly, HasConverter, IsRequiredForSelect, HasMarkup, HasCurrentMarkup,
    HasLabel, HasValidator, HasOnchange, HasValueChangeListener,
    HasValidatorMessage, HasConverterMessage, HasRequiredMessageForSelect, HasTabIndex, IsFocus, IsGridLayoutComponent,
    Select2, HasPlaceholder {

  /**
   * The value of the multi select.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List"})
  void setValue(String value);



  /**
   * A javascript callback that handles automatic tokenization of free-text entry.
   */
  @TagAttribute()
  @UIComponentTagAttribute(generate = false)
  void setTokenizer(String tokenizer);

  /**
   * The list of characters that should be used as token separators.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.String[]", generate = false)
  void setTokenSeparators(String tokenSeparators);

  /**
   * Hide the dropdown, this is only useful with allowCustom=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false", generate = false)
  void setHideDropdown(String hideDropdown);
}
