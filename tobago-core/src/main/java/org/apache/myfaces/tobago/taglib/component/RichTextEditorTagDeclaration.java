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

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;

/*
 * Created: Aug 5, 2005 4:58:19 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a text editor.
 */
// TODO: switched off @Tag(name="richTextEditor")
public interface RichTextEditorTagDeclaration extends TextInputTagDeclaration, HasIdBindingAndRendered, HasValue, HasLabelAndAccessKey, HasWidth {
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean")
  void setStatePreview(String statePreview);
}
