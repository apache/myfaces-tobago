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

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionListener;
import java.util.Iterator;

public abstract class AbstractResetInputActionListener implements ActionListener {

  protected void resetChildren(UIComponent component) {
    Iterator it = component.getFacetsAndChildren();
    while (it.hasNext()) {
      UIComponent child = (UIComponent) it.next();
      if (child instanceof EditableValueHolder) {
        reset((EditableValueHolder) child);
      }
      resetChildren(child);
    }
  }

  public static void reset(EditableValueHolder editableValueHolder) {
    editableValueHolder.setValue(null);
    editableValueHolder.setSubmittedValue(null);
    editableValueHolder.setLocalValueSet(false);
    editableValueHolder.setValid(true);
  }
}
