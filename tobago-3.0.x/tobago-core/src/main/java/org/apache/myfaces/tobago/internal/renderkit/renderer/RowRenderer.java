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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

public class RowRenderer extends DecodingCommandRendererBase {

  // XXX hack to fix TOBAGO-1572
  @Override
  protected void commandActivated(UIComponent component) {

    AbstractUIEvent event = null;
    for (UIComponent uiComponent : component.getChildren()) {
      if (uiComponent instanceof AbstractUIEvent) {
        event = (AbstractUIEvent) uiComponent;
      }
    }
    if (event != null) {
      event.queueEvent(new ActionEvent(event));
    } else {
      component.queueEvent(new ActionEvent(component));
    }
  }
}
