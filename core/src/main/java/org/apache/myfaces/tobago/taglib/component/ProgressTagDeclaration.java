package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasTip;

/*
 * Created: Aug 5, 2005 3:55:04 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a progressbar.
 */
@Tag(name = "progress", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "javax.faces.component.UIOutput",
    rendererType = "Progress", isComponentAlreadyDefined = true)
public interface ProgressTagDeclaration extends BeanTagDeclaration, HasIdBindingAndRendered, HasTip {

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = { "javax.swing.BoundedRangeModel" })
  void setValue(String value);
}
