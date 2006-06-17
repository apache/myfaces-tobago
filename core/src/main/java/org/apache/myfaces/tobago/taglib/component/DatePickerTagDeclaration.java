package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_DATE_PICKER;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.05.2006
 * Time: 19:17:28
 * To change this template use File | Settings | File Templates.
 */
@Tag(name = "datePicker")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIDatePicker",
    rendererType = RENDERER_TYPE_DATE_PICKER)
public interface DatePickerTagDeclaration  extends TobagoTagDeclaration, HasFor {
}
