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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;

import javax.faces.component.UIForm;

@Tag(name = "form")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIForm",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIForm",
    uiComponentFacesClass = "javax.faces.component.UIForm",
    interfaces = "org.apache.myfaces.tobago.component.Form",
    componentFamily = UIForm.COMPONENT_FAMILY,
    rendererType = RendererTypes.FORM,
    isTransparentForLayout = true,
    allowedChildComponenents = "ALL")
public interface FormTagDeclaration extends HasBinding, HasId {

  /**
   *

   @TagAttribute
   @UIComponentTagAttribute(type = "java.lang.String")
   void setNotFor(String notFor);
   */
}
