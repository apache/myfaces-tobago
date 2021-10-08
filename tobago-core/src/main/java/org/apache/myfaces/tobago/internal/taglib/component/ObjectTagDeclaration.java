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

import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIOutput;

/**
 * Show external content inside of an application.
 * This will typically renders an iframe tag.
 */
@Tag(name = "object")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIObject",
    uiComponentFacesClass = "jakarta.faces.component.UIOutput",
    componentFamily = UIOutput.COMPONENT_FAMILY,
    rendererType = RendererTypes.OBJECT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "NONE",
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SPREAD,
            description = "Use the full height for the HTML content."
        )
    })
public interface ObjectTagDeclaration extends HasIdBindingAndRendered, IsVisual {

  /**
   * URI to object source
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setSrc(String src);

  /**
   * Name of the element.
   * If not set the id will be used as name. The id in JSF normally contains colons.
   * This doesn't work in Internet Explorer 9 and lower when using window.open(src, target).
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setName(String name);

  /**
   * Value of the iframe sandbox attribute.
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setSandbox(String sandbox);
}
