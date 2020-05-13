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

import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PreRenderComponentEvent;
import javax.swing.BoundedRangeModel;

@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUIStars extends UIInput
    implements SupportFieldId, SupportsLabelLayout, Visual, ClientBehaviorHolder {

  private int rangeValue;
  private int rangeMax;

  @Override
  public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {

    super.processEvent(event);

    if (event instanceof PreRenderComponentEvent) {
      Object model = getValue();
      if (model instanceof BoundedRangeModel) {
        BoundedRangeModel boundedRangeModel = (BoundedRangeModel) model;
        rangeValue = boundedRangeModel.getValue();
        rangeMax = boundedRangeModel.getMaximum();
      } else {
        if (model instanceof Number) {
          rangeValue = ((Number) model).intValue();
        } else if (model != null && !model.equals("")) {
          rangeValue = Integer.valueOf("" + model);
        } else {
          rangeValue = 0;
        }
        rangeMax = getMax() != null ? getMax() : 5;
      }

      if (rangeMax <= 0) {
        rangeMax = 5;
      }
      if (rangeValue > rangeMax) {
        rangeValue = rangeMax;
      }
    }
  }

  public abstract boolean isFocus();

  public abstract boolean isReadonly();

  public abstract boolean isDisabled();

  public abstract Integer getMax();

  public abstract Double getPlaceholder();

  public abstract Integer getTabIndex();

  public int getRangeValue() {
    return rangeValue;
  }

  public int getRangeMax() {
    return rangeMax;
  }

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "field";
  }
}
