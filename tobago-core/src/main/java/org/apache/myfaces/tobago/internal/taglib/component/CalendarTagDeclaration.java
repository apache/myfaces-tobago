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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasStyle;

import javax.faces.component.UIOutput;

/**
 * Renders a calendar.
 */
@Tag(name = "calendar")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UICalendar",
    uiComponentBaseClass = "javax.faces.component.UIOutput",
    uiComponentFacesClass = "javax.faces.component.UIOutput",
    interfaces = "org.apache.myfaces.tobago.config.Configurable",
    componentFamily = UIOutput.COMPONENT_FAMILY,
    rendererType = RendererTypes.CALENDAR)
public interface CalendarTagDeclaration
    extends HasIdBindingAndRendered, HasStyle, HasMarkup, HasCurrentMarkup {

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"java.util.Calendar", "java.util.Date"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);
}
