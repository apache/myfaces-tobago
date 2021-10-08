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
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsPlain;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIForm;

/**
 * If an action is preformed, everything within the next parent form belongs to the submit-area.
 * An action could be a button which is pressed.
 * tc:page is a form.
 * Everything in the submit-area is written into the model, even other forms.
 */
@Tag(name = "form")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIForm",
    uiComponentFacesClass = "jakarta.faces.component.UIForm",
    componentFamily = UIForm.COMPONENT_FAMILY,
    rendererType = RendererTypes.FORM,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "ALL")
public interface FormTagDeclaration extends HasBinding, HasId, IsVisual, IsPlain {

  /**
   * Flag indicating this component should rendered as an inline element.
   *
   * @deprecated since 4.0.0. May use a subtag &lt;tc:style customClass="d-inline"/&gt; instead.
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setInline(String inline);

}
