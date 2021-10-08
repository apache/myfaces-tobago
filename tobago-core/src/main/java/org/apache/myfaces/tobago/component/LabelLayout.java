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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.internal.util.Deprecation;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

public enum LabelLayout {

  /**
   * do not render the label - same behavior as component without label attribute
   */
  none,

  /**
   * flex layout: let the label be on the left side
   */
  flexLeft,

  /**
   * flex layout: let the label be on the right side
   */
  flexRight,

  /**
   * let the label be on the top of the element
   */
  top,

  /**
   * segment layout: let the label be on the left side
   */
  segmentLeft,

  /**
   * segment layout: let the label be on the right side
   */
  segmentRight,

  /**
   * flow layout: let the label be on the left side
   */
  flowLeft,

  /**
   * flow layout: let the label be on the right side
   */
  flowRight,

  /**
   * skip rendering the surrounding container.
   *
   * @deprecated since 5.0.0, not needed, because there is no surrounding container.
   */
  @Deprecated
  skip,

  /**
   * grid layout: let the label be on the left cell and the input on the right cell. It uses 2 cells instead of one.
   */
  gridLeft,

  /**
   * grid layout: let the label be on the right cell and the input on the left cell. It uses 2 cells instead of one.
   */
  gridRight,

  /**
   * grid layout: let the label be on the top cell and the input on the bottom cell. It uses 2 cells instead of one.
   */
  gridTop,

  /**
   * grid layout: let the label be on the bottom cell and the input on the top cell. It uses 2 cells instead of one.
   */
  gridBottom;

  private static final String SEGMENT_TO_RENDER_KEY = LabelLayout.class.getName();

  public static boolean isSegment(final LabelLayout labelLayout) {
    return labelLayout == segmentLeft || labelLayout == segmentRight;
  }

  /**
   * @deprecated since 5.0.0. Please use {@link SupportsLabelLayout#setNextToRenderIsLabel(boolean)}.
   */
  @Deprecated
  public static void setSegment(final FacesContext facesContext, final LabelLayout labelLayout) {
    Deprecation.LOG.error("not longer supported - see javadoc");
  }

  /**
   * @deprecated since 5.0.0. Please use {@link SupportsLabelLayout#isNextToRenderIsLabel()}.
   */
  @Deprecated
  public static LabelLayout getSegment(final FacesContext facesContext) {
    Deprecation.LOG.error("not longer supported - see javadoc");
    return null;
  }

  public static void removeSegment(final FacesContext facesContext) {
    facesContext.getAttributes().remove(SEGMENT_TO_RENDER_KEY);
  }

  public static boolean isGridLeft(final UIComponent component) {
    return component instanceof SupportsLabelLayout
        && ((SupportsLabelLayout) component).getLabelLayout() == LabelLayout.gridLeft;
  }

  public static boolean isGridRight(final UIComponent component) {
    return component instanceof SupportsLabelLayout
        && ((SupportsLabelLayout) component).getLabelLayout() == LabelLayout.gridRight;
  }

  public static boolean isGridTop(final UIComponent component) {
    return component instanceof SupportsLabelLayout
        && ((SupportsLabelLayout) component).getLabelLayout() == LabelLayout.gridTop;
  }

  public static boolean isGridBottom(final UIComponent component) {
    return component instanceof SupportsLabelLayout
        && ((SupportsLabelLayout) component).getLabelLayout() == LabelLayout.gridBottom;
  }
}
