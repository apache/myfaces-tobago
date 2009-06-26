package org.apache.myfaces.tobago.layout.grid;

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

import org.apache.myfaces.tobago.layout.Component;

public class PseudoCell implements Cell {

  private RealCell realCell;
  private boolean inFirstColumn;
  private boolean inFirstRow;

  public PseudoCell(RealCell realCell, boolean inFirstColumn, boolean inFirstRow) {
    this.realCell = realCell;
    this.inFirstColumn = inFirstColumn;
    this.inFirstRow = inFirstRow;
  }

  public Component getComponent() {
    return realCell.getComponent();
  }

  public RealCell getRealCell() {
    return realCell;
  }

  public boolean isInFirstColumn() {
    return inFirstColumn;
  }

  public boolean isInFirstRow() {
    return inFirstRow;
  }
}
