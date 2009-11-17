package org.apache.myfaces.tobago.taglib.decl;

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
   * @param leftOffset The left offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getLeftOffset(getFacesContext(), this)")
  void setLeftOffset(String leftOffset);

  /**
   * This attribute is for internal use only.
   *
   * @param rightOffset The right offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getRightOffset(getFacesContext(), this)")
  void setRightOffset(String rightOffset);

  /**
   * This attribute is for internal use only.
   *
   * @param topOffset The top offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getTopOffset(getFacesContext(), this)")
  void setTopOffset(String topOffset);

  /**
   * This attribute is for internal use only.
   *
   * @param bottomOffset The bottom offset which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getBottomOffset(getFacesContext(), this)")
  void setBottomOffset(String bottomOffset);

}
