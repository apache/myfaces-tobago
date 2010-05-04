package org.apache.myfaces.tobago.internal.taglib.declaration;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface IsGridLayoutContainer {

  /**
   * This attribute is for internal use only.
   *
   * @param offsetLeft The left offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getOffsetLeft(getFacesContext(), this)")
  void setOffsetLeft(String offsetLeft);

  /**
   * This attribute is for internal use only.
   *
   * @param offsetRight The right offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getOffsetRight(getFacesContext(), this)")
  void setOffsetRight(String offsetRight);

  /**
   * This attribute is for internal use only.
   *
   * @param offsetTop The top offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getOffsetTop(getFacesContext(), this)")
  void setOffsetTop(String offsetTop);

  /**
   * This attribute is for internal use only.
   *
   * @param offsetBottom The bottom offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getOffsetBottom(getFacesContext(), this)")
  void setOffsetBottom(String offsetBottom);

}
