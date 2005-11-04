/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasValue;

/*
 * Created: Aug 5, 2005 4:08:32 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a 'hidden' input element.
 */
@Tag(name="hidden")
@UIComponentTag(UIComponent="org.apache.myfaces.tobago.component.UIInput")
public interface HiddenTagDeclaration extends BeanTagDeclaration, HasId, HasBinding, HasValue {

  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="true")
  void setInline(String inline);

}
