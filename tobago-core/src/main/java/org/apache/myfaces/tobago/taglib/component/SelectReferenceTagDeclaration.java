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

import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 31.03.2006
 * Time: 22:02:37
 * To change this template use File | Settings | File Templates.
 */

/**
 * Renders a set of option related to and same type as the <strong>for</strong>
 * component.
 */
@Tag(name = "selectReference", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "javax.faces.component.UIOutput",
    rendererType = "SelectReference")
public interface SelectReferenceTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered {
  /**
   * Id of the component, this is related to.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setFor(String forComponent);

  /**
   * Range of items to render.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setRenderRange(String renderRange);
}
