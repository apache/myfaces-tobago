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

import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.event.ActionListener;

import java.util.Iterator;

public abstract class AbstractResetInputActionListener implements ActionListener {

  protected void resetChildren(final UIComponent component) {
    final Iterator<UIComponent> it = component.getFacetsAndChildren();
    while (it.hasNext()) {
      final UIComponent child = it.next();
      if (child instanceof EditableValueHolder) {
        reset((EditableValueHolder) child);
      }
      resetChildren(child);
    }
  }

  public static void reset(final EditableValueHolder editableValueHolder) {
    editableValueHolder.setValue(null);
    editableValueHolder.setSubmittedValue(null);
    editableValueHolder.setLocalValueSet(false);
    editableValueHolder.setValid(true);
  }
}
