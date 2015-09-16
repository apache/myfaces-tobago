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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessageForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasStyle;
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

import javax.faces.component.UISelectMany;

/**
 * Render a multi selection option listbox.
 */
@Tag(name = "selectManyListbox")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectManyListbox",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectMany",
    uiComponentFacesClass = "javax.faces.component.UISelectMany",
    componentFamily = UISelectMany.COMPONENT_FAMILY,
    rendererType = RendererTypes.SELECT_MANY_LISTBOX,
    allowedChildComponenents = {"javax.faces.SelectItem", "javax.faces.SelectItems"})

public interface SelectManyListboxTagDeclaration
    extends HasId, IsDisabled, IsRendered, HasBinding, HasTip,
    IsReadonly, HasConverter, IsRequiredForSelect, HasMarkup, HasCurrentMarkup,
    HasLabel, HasValidator, HasValueChangeListener, HasLabelLayout,
    HasValidatorMessage, HasConverterMessage, HasRequiredMessageForSelect, HasTabIndex, IsFocus, IsGridLayoutComponent,
    HasStyle {

  /**
   * The value of the multi select.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List"})
  void setValue(String value);
}
