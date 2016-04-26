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

package org.apache.myfaces.tobago.internal.taglib.extension;

import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Renders a separator.
 * <br>
 * Short syntax of:
 * <pre>
 * &lt;tc:separator&gt;
 *   &lt;f:facet name="label"&gt;
 *     &lt;tc:label value="label"/&gt;
 *   &lt;/f:facet&gt;
 * &lt;/tc:separator&gt;
 * </pre>
 *
 * @deprecated since Tobago 3.0. The tx-library is deprecated, please use the tc-library.
 */
@Tag(
    name = "separator")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.component.SeparatorTag",
    componentType = "org.apache.myfaces.tobago.Separator",
    rendererType = "Separator",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.SeparatorExtensionHandler")
public interface SeparatorExtensionTag {

  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  void setBinding(final javax.el.ValueExpression binding);

  /**
   * Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setRendered(final javax.el.ValueExpression rendered);

  /**
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setLabel(final javax.el.ValueExpression label);
}
