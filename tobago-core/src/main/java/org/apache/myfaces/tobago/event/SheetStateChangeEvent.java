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
import jakarta.faces.event.FacesListener;
import java.io.Serial;

public class SheetStateChangeEvent extends ActionEvent {

  @Serial
  private static final long serialVersionUID = 2875570768774425451L;

  public SheetStateChangeEvent(final UIComponent uiComponent) {
    super(uiComponent);
  }

  @Override
  public boolean isAppropriateListener(final FacesListener facesListener) {
    return facesListener instanceof SheetStateChangeListener;
  }

  @Override
  public void processListener(final FacesListener facesListener) {
    if (facesListener instanceof SheetStateChangeListener) {
      ((SheetStateChangeListener) facesListener).processSheetStateChange(this);
    }
  }

}
