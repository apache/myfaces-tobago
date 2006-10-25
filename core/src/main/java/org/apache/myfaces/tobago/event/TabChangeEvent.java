package org.apache.myfaces.tobago.event;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * User: weber
 * Date: 13.12.2004
 * Time: 16:25:03
 */
public class TabChangeEvent extends FacesEvent {

  private Object oldState;
  private Object newState;

  public TabChangeEvent(UIComponent uiComponent, Object oldState, Object newState) {
    super(uiComponent);
    this.oldState = oldState;
    this.newState = newState;
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
    if (oldState instanceof Integer) {
      return ((Integer) oldState);
    }
    return -1;
  }

  public int getNewTabIndex() {
    if (newState instanceof Integer) {
      return ((Integer) newState);
    }
    return -1;
  }

  public Object getOldState() {
    return oldState;
  }

  public void setOldState(Object oldState) {
    this.oldState = oldState;
  }

  public Object getNewState() {
    return newState;
  }

  public void setNewState(Object newState) {
    this.newState = newState;
  }
}
