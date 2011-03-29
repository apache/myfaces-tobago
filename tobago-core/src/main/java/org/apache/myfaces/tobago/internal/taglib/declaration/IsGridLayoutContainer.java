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
   * @param borderLeft The left border which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getBorderLeft(getFacesContext(), this)")
  void setBorderLeft(String borderLeft);

  /**
   * This attribute is for internal use only.
   *
   * @param borderRight The right border which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getBorderRight(getFacesContext(), this)")
  void setBorderRight(String borderRight);

  /**
   * This attribute is for internal use only.
   *
   * @param borderTop The top border which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getBorderTop(getFacesContext(), this)")
  void setBorderTop(String borderTop);

  /**
   * This attribute is for internal use only.
   *
   * @param borderBottom The bottom border which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getBorderBottom(getFacesContext(), this)")
  void setBorderBottom(String borderBottom);

  /**
   * This attribute is for internal use only.
   *
   * @param paddingLeft The left padding which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getPaddingLeft(getFacesContext(), this)")
  void setPaddingLeft(String paddingLeft);

  /**
   * This attribute is for internal use only.
   *
   * @param paddingRight The right padding which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getPaddingRight(getFacesContext(), this)")
  void setPaddingRight(String paddingRight);

  /**
   * This attribute is for internal use only.
   *
   * @param paddingTop The top padding which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getPaddingTop(getFacesContext(), this)")
  void setPaddingTop(String paddingTop);

  /**
   * This attribute is for internal use only.
   *
   * @param paddingBottom The bottom padding which is needed by some containers (e. g. a box).
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "((org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer)\n"
          + "getRenderer(getFacesContext())).getPaddingBottom(getFacesContext(), this)")
  void setPaddingBottom(String paddingBottom);

  /**
   * This attribute is for internal use only.
   *
   * @param overflowX Does the component need a horizontal scollbar.
   */
  @UIComponentTagAttribute(
      type = "boolean")
  void setOverflowX(String overflowX);

  /**
   * This attribute is for internal use only.
   *
   * @param overflowY Does the component need a vertical scollbar.
   */
  @UIComponentTagAttribute(
      type = "boolean")
  void setOverflowY(String overflowY);

}
