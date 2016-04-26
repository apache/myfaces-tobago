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

import javax.faces.context.FacesContext;

public enum LabelLayout {

  /**
   * do not render the label
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
   */
  skip;

  private static final String SEGMENT_TO_RENDER_KEY = LabelLayout.class.getName();

  public static boolean isSegment(final LabelLayout labelLayout) {
    return labelLayout == segmentLeft || labelLayout == segmentRight;
  }

  public static void setSegment(final FacesContext facesContext, final LabelLayout labelLayout) {
    if (labelLayout != segmentLeft && labelLayout != segmentRight) {
      throw new IllegalArgumentException("not supported: " + labelLayout);
    }
    facesContext.getAttributes().put(SEGMENT_TO_RENDER_KEY, labelLayout);
  }

  public static LabelLayout getSegment(final FacesContext facesContext) {
    return (LabelLayout) facesContext.getAttributes().get(SEGMENT_TO_RENDER_KEY);
  }

  public static void removeSegment(FacesContext facesContext) {
    facesContext.getAttributes().remove(SEGMENT_TO_RENDER_KEY);
  }
}
