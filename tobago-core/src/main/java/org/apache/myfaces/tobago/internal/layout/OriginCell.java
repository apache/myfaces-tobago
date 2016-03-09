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

import javax.faces.component.UIComponent;

public class OriginCell implements Cell {

  private UIComponent component;
  private int columnSpan;
  private int rowSpan;

  public OriginCell(final UIComponent component) {
    this.component = component;
  }

  @Override
  public UIComponent getComponent() {
    return component;
  }

  @Override
  public OriginCell getOrigin() {
    return this;
  }

  @Override
  public boolean isVerticalFirst() {
    return true;
  }

  @Override
  public boolean isHorizontalFirst() {
    return true;
  }

  @Override
  public int getColumnSpan() {
    return columnSpan;
  }

  public void setColumnSpan(final int columnSpan) {
    this.columnSpan = columnSpan;
  }

  @Override
  public int getRowSpan() {
    return rowSpan;
  }

  public void setRowSpan(final int rowSpan) {
    this.rowSpan = rowSpan;
  }
}
