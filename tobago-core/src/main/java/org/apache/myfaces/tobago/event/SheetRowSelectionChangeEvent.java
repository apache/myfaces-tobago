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

package org.apache.myfaces.tobago.event;

import jakarta.faces.component.UIComponent;
import jakarta.faces.event.ActionEvent;

import java.io.Serial;
import java.util.List;

public class SheetRowSelectionChangeEvent extends ActionEvent {

  @Serial
  private static final long serialVersionUID = 1482246499642513120L;
  private final String sourceId;
  private final List<Integer> oldSelectedRows;
  private final List<Integer> newSelectedRows;

  /**
   * @param uiComponent     on which the event is queued
   * @param sourceId        of the current action
   * @param oldSelectedRows selected rows before the change
   * @param newSelectedRows selected rows after the change
   */
  public SheetRowSelectionChangeEvent(
      final UIComponent uiComponent, final String sourceId,
      final List<Integer> oldSelectedRows, final List<Integer> newSelectedRows) {
    super(uiComponent);
    this.sourceId = sourceId;
    this.oldSelectedRows = oldSelectedRows;
    this.newSelectedRows = newSelectedRows;
  }

  public String getSourceId() {
    return sourceId;
  }

  public List<Integer> getOldSelectedRows() {
    return oldSelectedRows;
  }

  public List<Integer> getNewSelectedRows() {
    return newSelectedRows;
  }
}
