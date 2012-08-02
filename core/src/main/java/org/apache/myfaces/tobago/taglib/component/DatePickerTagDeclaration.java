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

package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_DATE_PICKER;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;

/*
 * Date: 30.05.2006
 * Time: 19:17:28
 */

/**
 * Renders a date picker.
 * The component needs a DateFormat Pattern from a converter. The converter
 * should be an instance of DateTimeConverter and return a valid pattern from
 * the method getPattern()
 */
@Tag(name = "datePicker")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIDatePicker",
    rendererType = RENDERER_TYPE_DATE_PICKER)
public interface DatePickerTagDeclaration extends TobagoTagDeclaration, HasFor,
    HasTabIndex {
}
