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

import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * @deprecated This class should not be used directly, please use IsGridLayoutComponent. 
 */
@Deprecated
public interface IsGridLayoutComponentBase {

  /**
   * This value will usually be set by the layout manager. It holds the current width computed by the layout manager.
   *
   * @param currentWidth The current width for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getWidth()")
  void setCurrentWidth(String currentWidth);

  /**
   * This value will usually be set by the layout manager. It holds the current height computed by the layout manager.
   *
   * @param currentHeight The current height for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getHeight()")
  void setCurrentHeight(String currentHeight);
  
  /**
   * @param columnSpan The number of horizontal cells this component should use.
   */
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "1")
  void setColumnSpan(String columnSpan);

  /**
   * @param rowSpan The number of vertical cells this component should use.
   */
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "1")
  void setRowSpan(String rowSpan);

  /**
   * @param minimumWidth The minimum width for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMinimumWidth(getFacesContext(), this) : Measure.ZERO")
  void setMinimumWidth(String minimumWidth);

  /**
   * @param minimumHeight The minimum height for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMinimumHeight(getFacesContext(), this) : Measure.ZERO")
  void setMinimumHeight(String minimumHeight);

  /**
   * @param preferredWidth The preferred width for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getPreferredWidth(getFacesContext(), this) : Measure.ZERO")
  void setPreferredWidth(String preferredWidth);

  /**
   * @param preferredHeight The preferred height for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getPreferredHeight(getFacesContext(), this) : Measure.ZERO")
  void setPreferredHeight(String preferredHeight);

  /**
   * @param maximumWidth The maximum width for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMaximumWidth(getFacesContext(), this) : Measure.ZERO")
  void setMaximumWidth(String maximumWidth);

  /**
   * @param maximumHeight The maximum height for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMaximumHeight(getFacesContext(), this) : Measure.ZERO")
  void setMaximumHeight(String maximumHeight);

  /**
   * @param marginLeft The margin at the left of this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMarginLeft(getFacesContext(), this) : Measure.ZERO")
  void setMarginLeft(String marginLeft);

  /**
   * @param marginRight The margin at the right of this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMarginRight(getFacesContext(), this) : Measure.ZERO")
  void setMarginRight(String marginRight);

  /**
   * @param marginTop The margin at the top of this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMarginTop(getFacesContext(), this) : Measure.ZERO")
  void setMarginTop(String marginTop);

  /**
   * @param marginBottom The margin at the bottom of this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getRendererType() != null ? ((LayoutComponentRenderer) "
          + "getRenderer(getFacesContext())).getMarginBottom(getFacesContext(), this) : Measure.ZERO")
  void setMarginBottom(String marginBottom);

  /**
   * This value will usually be set by the layout manager.
   *
   * @param left The left position value for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setLeft(String left);

  /**
   * This value will usually be set by the layout manager.
   *
   * @param top The top position value for this component.
   */
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setTop(String top);

  /**
   * This attribute is for internal use only.
   *
   * @param horizontalIndex The index of the component inside its container grid in horizontal direction.
   */
  @UIComponentTagAttribute(type = "java.lang.Integer")
  void setHorizontalIndex(String horizontalIndex);

  /**
   * This attribute is for internal use only.
   *
   * @param verticalIndex The index of the component inside its container grid in vertical direction.
   */
  @UIComponentTagAttribute(type = "java.lang.Integer")
  void setVerticalIndex(String verticalIndex);

}
