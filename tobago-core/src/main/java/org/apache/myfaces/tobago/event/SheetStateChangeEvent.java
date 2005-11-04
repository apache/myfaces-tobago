/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * User: weber
 * Date: 13.12.2004
 * Time: 16:25:03
 */
public class SheetStateChangeEvent extends FacesEvent {

  public SheetStateChangeEvent(UIComponent uiComponent) {
    super(uiComponent);
  }

  public boolean isAppropriateListener(FacesListener facesListener) {
    return true;    //todo
  }

  public void processListener(FacesListener facesListener) {
    //todo
  }

}
