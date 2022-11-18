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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.component.StateHolder;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;

public class ValueExpressionTabChangeListener implements TabChangeListener, StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String type;
  private ValueExpression valueExpression;

  public ValueExpressionTabChangeListener() {
  }

  public ValueExpressionTabChangeListener(final String type, final ValueExpression valueExpression) {
    this.type = type;
    this.valueExpression = valueExpression;
  }

  @Override
  public void processTabChange(final TabChangeEvent tabChangeEvent) {
    final TabChangeListener handler = getTabChangeListener();
    if (handler != null) {
      handler.processTabChange(tabChangeEvent);
    } else {
      LOG.error("Ignoring TabChangeEvent. No TabChangeListener found.");
    }
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] state = new Object[2];
    state[0] = UIComponentBase.saveAttachedState(context, valueExpression);
    state[1] = type;
    return state;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    valueExpression = (ValueExpression) UIComponentBase.restoreAttachedState(context, values[0]);
    type = (String) values[1];

  }

  @Override
  public boolean isTransient() {
    return false;
  }

  @Override
  public void setTransient(final boolean newTransientValue) {
    // ignore
  }

  private TabChangeListener getTabChangeListener() {
    TabChangeListener handler = null;
    if (valueExpression != null) {
      final Object obj = valueExpression.getValue(FacesContext.getCurrentInstance().getELContext());
      if (obj != null && obj instanceof TabChangeListener) {
        handler = (TabChangeListener) obj;
      }
    }
    if (handler == null && type != null) {
      handler = createTabChangeListener(type);
      if (handler != null && valueExpression != null) {
        valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), handler);
      }
    }
    return handler;
  }

  private TabChangeListener createTabChangeListener(final String className) {
    try {
      final Class clazz = getClass().getClassLoader().loadClass(className);
      return (TabChangeListener) clazz.newInstance();
    } catch (final Exception e) {
      LOG.error("", e);
    }
    return null;
  }
}
