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

public interface HasSpacing {

  /**
   * Spacing between component and layout cell's.
   * Can be overwritten by columnSpacing and rowSpacing.
   *
   * @deprecated Use columnSpacing and/or rowSpacing instead.
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure")
  void setCellspacing(String cellspacing);

  /**
   * Spacing between the columns in the actual layout.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getCellspacing() != null\n" 
          + " ? getCellspacing()\n : getRendererType() != null"
          + " ? ((SpacingValues)getRenderer(getFacesContext())).getColumnSpacing(getFacesContext(), this)"
          + " : Measure.ZERO")
  void setColumnSpacing(String columnSpacing);

  /**
   * Spacing between the rows in the actual layout.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Measure",
      defaultCode = "getCellspacing() != null\n" 
          + " ? getCellspacing()\n : getRendererType() != null"
          + " ? ((SpacingValues)getRenderer(getFacesContext())).getRowSpacing(getFacesContext(), this) : Measure.ZERO")
  void setRowSpacing(String rowSpacing);
}
