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

package org.apache.myfaces.tobago.internal.component;

import jakarta.faces.component.UIData;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import org.apache.myfaces.tobago.application.Toast;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.renderkit.renderer.ToastsRenderer;
import org.apache.myfaces.tobago.layout.Placement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUIToasts extends UIData implements ClientBehaviorHolder, Visual {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String STATES = "states";

  @Override
  public Collection<Toast> getValue() {
    final Object value = super.getValue();
    if (value instanceof Collection) {
      return (Collection<Toast>) value;
    } else {
      return Collections.emptyList();
    }
  }

  public Map<String, ToastsRenderer.StateData> getStates() {
    Object hideTime = getStateHelper().eval(STATES);
    if (hideTime instanceof Map) {
      return (Map<String, ToastsRenderer.StateData>) hideTime;
    }
    return new HashMap<>();
  }

  public void setStates(Map<String, ToastsRenderer.StateData> states) {
    getStateHelper().put(STATES, states);
  }

  public abstract Placement getPlacement();

  public abstract Integer getDisposeDelay();

  private enum PropertyKeys {
    showedIds, hideTimes, closedIds
  }
}
