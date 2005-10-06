/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.*;

/*
 * Created: Aug 5, 2005 5:03:15 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a date input field.
 */
@Tag(name="time")
@UIComponentTag(UIComponent="org.apache.myfaces.tobago.component.UIInput", RendererType=RENDERER_TYPE_TIME)
public interface TimeTagDeclaration extends InputTagDeclaration, HasIdBindingAndRendered, HasValue, IsReadonly, IsDisabled, IsInline, HasLabelAndAccessKey, HasTip {

}
