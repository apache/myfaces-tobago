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
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFor;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;

/**
 * Renders a date picker.
 * The component needs a DateFormat Pattern from a converter. The converter
 * should be an instance of DateTimeConverter and return a valid pattern from
 * the method getPattern()
 */
@Tag(name = "datePicker")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIDatePicker",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIDatePicker",
    rendererType = RendererTypes.DATE_PICKER,
    allowedChildComponenents = "NONE")
public interface DatePickerTagDeclaration
    extends HasFor, HasTabIndex, HasIdBindingAndRendered, HasMarkup, HasCurrentMarkup, IsGridLayoutComponent {

  @UIComponentTagAttribute()
  void setLink(String link);

  @UIComponentTagAttribute()
  void setResource(String resource);

  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setJsfResource(String jsfResource);

  @UIComponentTagAttribute()
  void setOnclick(String onclick);

  @UIComponentTagAttribute()
  void setTip(String tip);

  @UIComponentTagAttribute()
  void setImage(String image);

  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setDisabled(String disabled);

  @UIComponentTagAttribute(type = "java.lang.String[]")
  void setRenderedPartially(String componentIds);
}
