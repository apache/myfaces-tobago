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
 * This tag adds script files to include into the rendered page.
 * <br>
 * Some features are deprecated (because of CSP): This tag adds client side script to the rendered page.
 */
@Tag(name = "script")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIScript",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIScript",
    componentFamily = "org.apache.myfaces.tobago.Script",
    faceletHandler = "org.apache.myfaces.tobago.facelets.ScriptHandler",
    rendererType = RendererTypes.SCRIPT,
    allowedChildComponenents = "NONE")
public interface ScriptTagDeclaration extends HasIdBindingAndRendered {

  /**
   * File name to include into the rendered page. The name must be full qualified, or relative.
   * If using a complete path from root, you'll need to add the contextPath from the web application.
   * This can be done with the EL #{request.contextPath}.
   * @param file A JavaScript file.
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  void setFile(String file);

}
