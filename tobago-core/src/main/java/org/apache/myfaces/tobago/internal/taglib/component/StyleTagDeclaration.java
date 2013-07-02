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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;

/**
 * Add a style tag with the given file name.
 * <p/>
 * Remark: Inline styles are deprecated because of CSP.
 */
@Tag(name = "style", bodyContent = BodyContent.TAGDEPENDENT)
@BodyContentDescription(contentType = "css")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIStyle",
    componentFamily = "org.apache.myfaces.tobago.Style",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    faceletHandler = "org.apache.myfaces.tobago.facelets.StyleHandler",
    rendererType = RendererTypes.STYLE,
    isTransparentForLayout = true,
    allowedChildComponenents = "NONE")
public interface StyleTagDeclaration extends HasIdBindingAndRendered {

  /**
   * Name of the stylesheet file to add to page.
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  void setFile(String file);

  /**
   * stylesheet to add to page.
   * @deprecated inline styles are deprecated because of CSP
   */
  @Deprecated
  @TagAttribute(bodyContent = true)
  @UIComponentTagAttribute()
  void setStyle(String style);

}
