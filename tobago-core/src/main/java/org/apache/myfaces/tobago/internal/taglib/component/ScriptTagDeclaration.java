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

/**
 * This tag adds script files to include to the rendered page.
 * Deprecated (CSP): This tag adds client side script to the rendered page.
 */
@Tag(name = "script")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIScript",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.apache.myfaces.tobago.Script",
    rendererType = RendererTypes.SCRIPT,
    isTransparentForLayout = true,
    allowedChildComponenents = "NONE")
public interface ScriptTagDeclaration extends HasIdBindingAndRendered {
  @TagAttribute()
  @UIComponentTagAttribute()
  void setFile(String file);

  /**
   * @deprecated Since 1.6.0. Please include a custom script file via the file attribute and use
   * <code>Tobago.registerListener(myFunction, Tobago.Phase.DOCUMENT_READY);</code> or
   * <code>Tobago.registerListener(myFunction, Tobago.Phase.WINDOW_LOAD);</code>
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute()
  void setOnload(String onload);

  /**
   * @deprecated Since 1.6.0. Please include a custom script file via the file attribute and use
   * <code>Tobago.registerListener(myFunction, Tobago.Phase.BEFORE_UNLOAD);</code>
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute()
  void setOnunload(String onunload);

  /**
   * @deprecated Since 1.6.0. Please include a custom script file via the file attribute and use
   * <code>Tobago.registerListener(myFunction, Tobago.Phase.BEFORE_EXIT);</code>
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute()
  void setOnexit(String onexit);

  /**
   * @deprecated Since 1.6.0. Please include a custom script file via the file attribute and use
   * <code>Tobago.registerListener(myFunction, Tobago.Phase.BEFORE_SUBMIT);</code>
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute()
  void setOnsubmit(String onsubmit);

  /**
   * @deprecated Since 1.6.0. Please include a custom script file via the file attribute.
   */
  @Deprecated
  @TagAttribute(bodyContent = true)
  @UIComponentTagAttribute()
  void setScript(String script);

}
