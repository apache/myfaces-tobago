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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;

import jakarta.faces.component.UIOutput;

/**
 * Renders a set of option related to and same type as the <strong>for</strong>
 * component.
 */
@Tag(name = "selectReference")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectReference",
    uiComponentFacesClass = "jakarta.faces.component.UIOutput",
    componentFamily = UIOutput.COMPONENT_FAMILY,
    rendererType = RendererTypes.SELECT_REFERENCE,
    allowedChildComponents = "NONE")
public interface SelectReferenceTagDeclaration extends HasIdBindingAndRendered {
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
