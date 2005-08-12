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
package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:18:50 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a checkbox.
 */
@Tag(name="selectBooleanCheckbox")
@BodyContentDescription(anyTagOf="<f:facet>* " )
@UIComponentTag(UIComponent="javax.faces.component.UISelectBoolean")
public interface SelectBooleanCheckboxTag extends InputTag, HasIdBindingAndRendered, HasLabelAndAccessKey, HasBooleanValue, IsDisabled, IsInline, HasTip {

}
