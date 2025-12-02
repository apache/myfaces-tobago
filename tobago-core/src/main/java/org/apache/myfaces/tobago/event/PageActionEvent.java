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

import org.apache.myfaces.tobago.internal.util.JsonUtils;

import jakarta.faces.component.UIComponent;
import jakarta.faces.event.ActionEvent;
import java.io.Serial;

public class PageActionEvent extends ActionEvent {

  @Serial
  private static final long serialVersionUID = 3364193750247386220L;

  private final SheetAction action;
  private int value;

  public PageActionEvent(final UIComponent component, final SheetAction action) {
    super(component);
    this.action = action;
  }

  public PageActionEvent(final UIComponent component, final JsonUtils.SheetActionRecord sheetActionRecord) {
    super(component);
    this.action = sheetActionRecord.action();
    final Integer target = sheetActionRecord.target();
    if (target != null) {
      this.value = target;
    }
  }

  /**
   * Returns the action type ({@link SheetAction}).
   */
  public SheetAction getAction() {
    return action;
  }

  public void setValue(final int value) {
    this.value = value;
  }

  /**
   * Returns the value for action types {@link SheetAction#toRow}
   * and {@link SheetAction#toPage}.
   */
  public int getValue() {
    return value;
  }

}
