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

package org.apache.myfaces.tobago.util;

import jakarta.faces.component.UIComponent;

public class SearchOnce {
  private transient boolean findChildSearched = false;
  private transient UIComponent findChildComponent = null;

  public <T extends UIComponent> T findChild(final UIComponent component, final Class<T> type) {
    if (!findChildSearched) {
      findChildComponent = ComponentUtils.findChild(component, type);
      findChildSearched = true;
    }
    return type.cast(findChildComponent);
  }
}
