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

import org.apache.myfaces.tobago.component.Visual;

import jakarta.faces.component.UIOutput;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ComponentSystemEventListener;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PreRenderComponentEvent;

import javax.swing.BoundedRangeModel;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.ProgressTagDeclaration}
 */
@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUIProgress extends UIOutput
    implements Visual, ComponentSystemEventListener, ClientBehaviorHolder {

  private double rangeValue;
  private double rangeMax;

  public double getRangeValue() {
    return rangeValue;
  }

  public double getRangeMax() {
    return rangeMax;
  }

  @Override
  public void processEvent(final ComponentSystemEvent event) throws AbortProcessingException {

    super.processEvent(event);

    if (event instanceof PreRenderComponentEvent) {
      final Object model = getValue();
      if (model instanceof BoundedRangeModel) {
        final BoundedRangeModel m = (BoundedRangeModel) model;
        rangeValue = (double) m.getValue();
        rangeMax = (double) m.getMaximum();
        final int min = m.getMinimum();
        if (min != 0) {
          rangeValue -= min;
          rangeMax -= min;
        }
      } else {
        if (model instanceof Number) {
          rangeValue = ((Number) model).doubleValue();
        } else if (model != null) {
          rangeValue = Double.parseDouble("" + model);
        }
        if (getMax() != null) {
          rangeMax = getMax();
        } else {
          rangeMax = 1.0;
        }
        if (rangeValue > rangeMax) {
          rangeValue = rangeMax;
        }
      }
    }
  }

  public abstract Double getMax();
}
