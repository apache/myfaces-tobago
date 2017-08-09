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

package org.apache.myfaces.tobago.internal.taglib.declaration;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface IsVisual {

  /**
   * For internal use. Used for rendering, if there is a child tag &lt;tc:style&gt;
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.renderkit.css.Style")
  void setStyle(String style);

  /**
   * Sets a CSS class in its parent, if the parent supports it.
   *
   * Which this feature it is possible to put a CSS class name into a component with the &lt;tc:style&gt; tag. Example:
   *
   * <pre>
   * &lt;tc:in&gt;
   *   &lt;tc:style customClass="my-emphasized"/&gt;
   * &lt;/tc:in&gt;
   * </pre>
   *
   * One capability is, to used external CSS libs.
   * <br>
   * This feature should not be used imprudent.
   * Because it might be unstable against changes in the renderered HTML code.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.renderkit.css.CustomClass")
  void setCustomClass(String customClass);

  /**
   * Indicate markup of this component.
   * The allowed markups can be defined or overridden in the theme.
   * The value 'none' should not be used any longer. Just leave the attribute empty, or use a NULL pointer.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.context.Markup")
  void setMarkup(String markup);

}
