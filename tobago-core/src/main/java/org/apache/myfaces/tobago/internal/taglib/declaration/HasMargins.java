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

public interface HasMargins {

  /**
   * Left margin between container component and the children.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getMargin() != null\n"
          + " ? getMargin()\n"
          + " : ((MarginValues)getRenderer(getFacesContext())).getMarginLeft(getFacesContext(), this)")
  void setMarginLeft(String margin);

  /**
   * Right margin between container component and the children.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getMargin() != null\n"
          + " ? getMargin()\n"
          + " : ((MarginValues)getRenderer(getFacesContext())).getMarginRight(getFacesContext(), this)")
  void setMarginRight(String margin);

  /**
   * Top margin between container component and the children.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getMargin() != null\n"
          + " ? getMargin()\n"
          + " : ((MarginValues)getRenderer(getFacesContext())).getMarginTop(getFacesContext(), this)")
  void setMarginTop(String margin);

  /**
   * Bottom margin between container component and the children.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getMargin() != null\n"
          + " ? getMargin()\n"
          + " : ((MarginValues)getRenderer(getFacesContext())).getMarginBottom(getFacesContext(), this)")
  void setMarginBottom(String margin);

}
