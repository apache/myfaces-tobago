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

package org.apache.myfaces.tobago.internal.layout;

import org.apache.myfaces.tobago.layout.LayoutComponent;

/**
 * To support horizontal and vertical spans, at each position will be a cell as a represantive.
 * Either a "origin cell" for the first position or a "span cell" for spanned other positions.
 */
public interface Cell {

  LayoutComponent getComponent();

  OriginCell getOrigin();

  /**
   * Is the origin cell or span cell at the first position of the cell compound.
   *
   * @return If its at the first position.
   */
  boolean isHorizontalFirst();

  /**
   * Is the origin cell or span cell at the first position of the cell compound.
   *
   * @return If its at the first position.
   */
  boolean isVerticalFirst();
}
