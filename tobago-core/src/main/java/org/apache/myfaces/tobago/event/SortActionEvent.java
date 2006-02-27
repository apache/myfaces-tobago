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

import org.apache.myfaces.tobago.component.UIData;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

public class SortActionEvent extends ActionEvent {

  public SortActionEvent(UIComponent uiComponent) {
    super(uiComponent);
  }

  /**
   * Convenience method to get the UIColumn to sort.
   */
  public UIColumn getColumn() {
    return (UIColumn) getComponent().getParent();
  }

  /**
   * Convenience method to get the UIData Component. 
   */
  public UIData getSheet() {
    return (UIData) getColumn().getParent();
  }
}
