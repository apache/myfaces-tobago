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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.*;

/*
 * Created: Aug 5, 2005 6:08:24 PM
 * User: bommel
 * $Id: $
 */
/**
 * Render a single selection option listbox.
 */
@Tag(name="selectOneListbox")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<tc:selectItem>)+ <f:facet>* " )
public interface SelectOneListboxTagDeclaration extends SelectOneTagDeclaration, HasId, HasValue, IsDisabled, IsReadonly, HasOnchangeListener, HasLabelAndAccessKey, IsRendered, HasBinding, HasHeight, HasTip {

  /**
   * Flag indicating that selecting an Item representing a Value is Required.
   * If an SelectItem was choosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute(type=String.class)
  @UIComponentTagAttribute(type="java.lang.Boolean")
  void setRequired(String required);
}
