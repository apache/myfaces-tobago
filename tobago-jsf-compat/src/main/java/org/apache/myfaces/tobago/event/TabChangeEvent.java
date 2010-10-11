package org.apache.myfaces.tobago.event;

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

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesListener;

/*
 * User: weber
 * Date: 13.12.2004
 * Time: 16:25:03
 */
public class TabChangeEvent extends ActionEvent {

  private static final long serialVersionUID = 422186716954088729L;

  private Integer oldTabIndex;
  private Integer newTabIndex;

  public TabChangeEvent(UIComponent uiComponent, Integer oldTabIndex, Integer newTabIndex) {
    super(uiComponent);
    this.oldTabIndex = oldTabIndex;
    this.newTabIndex = newTabIndex;
  }
  
  /**
   * @deprecated Please use {@link TabChangeEvent(UIComponent, Integer, Integer)}
   */
  @Deprecated
  public TabChangeEvent(UIComponent uiComponent, Object oldState, Object newState) {
    super(uiComponent);
    setOldState(oldState);
    setNewState(newState);
  }

  public boolean isAppropriateListener(FacesListener facesListener) {
    return facesListener instanceof TabChangeListener;
  }

  public void processListener(FacesListener facesListener) {
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
  @Deprecated
  public Object getOldState() {
    return oldTabIndex;
  }

  /**
   * @deprecated Not supported anymore
   */
  @Deprecated
  public void setOldState(Object oldTabIndex) {
    this.oldTabIndex = oldTabIndex instanceof Integer ? (Integer) oldTabIndex : -1;
  }

  /**
   * @deprecated Please use {@link #getNewTabIndex()}
   */
  @Deprecated
  public Object getNewState() {
    return newTabIndex;
  }

  /**
   * @deprecated Not supported anymore
   */
  @Deprecated
  public void setNewState(Object newTabIndex) {
    this.newTabIndex = newTabIndex instanceof Integer ? (Integer) newTabIndex : -1;
  }
}
