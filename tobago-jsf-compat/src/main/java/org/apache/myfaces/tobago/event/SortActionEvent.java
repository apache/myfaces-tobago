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

import javax.faces.component.UIColumn;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

public class SortActionEvent extends ActionEvent {

  private static final long serialVersionUID = 4906962574581815720L;

  private transient UIColumn column;

  public SortActionEvent(UIData sheet, UIColumn column) {
    super(sheet);
    this.column = column;
    setPhaseId(PhaseId.INVOKE_APPLICATION);
  }

  /**
   * @return UIColumn The UIColumn object for which this event was triggered.
   */
  public UIColumn getColumn() {
    return column;
  }

  public UIData getSheet() {
    return (UIData) getComponent();
  }
}
