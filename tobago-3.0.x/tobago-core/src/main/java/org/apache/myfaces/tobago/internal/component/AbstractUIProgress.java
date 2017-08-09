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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PreRenderComponentEvent;
import javax.swing.BoundedRangeModel;

@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUIProgress extends UIOutput
    implements Visual, ComponentSystemEventListener, ClientBehaviorHolder {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIProgress.class);

  private double rangeValue;
  private double rangeMax;

  public double getRangeValue() {
    return rangeValue;
  }

  public double getRangeMax() {
    return rangeMax;
  }

  @Override
  public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {

    super.processEvent(event);

    if (event instanceof PreRenderComponentEvent) {
      Object model = getValue();
      if (model instanceof BoundedRangeModel) {
        BoundedRangeModel m = (BoundedRangeModel) model;
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
