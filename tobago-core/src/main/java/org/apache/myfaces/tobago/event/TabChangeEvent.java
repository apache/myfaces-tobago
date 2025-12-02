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

public class TabChangeEvent extends ActionEvent {

  @Serial
  private static final long serialVersionUID = 422186716954088729L;

  private Integer oldTabIndex;
  private Integer newTabIndex;

  public TabChangeEvent(final UIComponent uiComponent, final Integer oldTabIndex, final Integer newTabIndex) {
    super(uiComponent);
    this.oldTabIndex = oldTabIndex;
    this.newTabIndex = newTabIndex;
  }

  /**
   * @deprecated Please use {@link #TabChangeEvent(UIComponent, Integer, Integer)}
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  public TabChangeEvent(final UIComponent uiComponent, final Object oldState, final Object newState) {
    super(uiComponent);
    setOldState(oldState);
    setNewState(newState);
  }

  @Override
  public boolean isAppropriateListener(final FacesListener facesListener) {
    return facesListener instanceof TabChangeListener;
  }

  @Override
  public void processListener(final FacesListener facesListener) {
    if (facesListener instanceof TabChangeListener) {
      ((TabChangeListener) facesListener).processTabChange(this);
    }
  }

  public int getOldTabIndex() {
    return oldTabIndex;
  }

  public int getNewTabIndex() {
    return newTabIndex;
  }

  /**
   * @deprecated Please use {@link #getOldTabIndex()}
   */
  @Deprecated(since = "1.0.31", forRemoval = true)
  public Object getOldState() {
    return oldTabIndex;
  }

  /**
   * @deprecated Not supported anymore
   */
  @Deprecated(since = "1.0.31", forRemoval = true)
  public void setOldState(final Object oldState) {
    this.oldTabIndex = oldState instanceof Integer ? (Integer) oldState : -1;
  }

  /**
   * @deprecated Please use {@link #getNewTabIndex()}
   */
  @Deprecated(since = "1.0.31", forRemoval = true)
  public Object getNewState() {
    return newTabIndex;
  }

  /**
   * @deprecated Not supported anymore
   */
  @Deprecated(since = "1.0.31", forRemoval = true)
  public void setNewState(final Object newState) {
    this.newTabIndex = newState instanceof Integer ? (Integer) newState : -1;
  }
}
