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

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/*
 * Date: Jan 9, 2007
 * Time: 8:08:01 PM
 */

@SuppressWarnings("deprecation")
public class ValueBindingTabChangeListener implements TabChangeListener, StateHolder {
  private static final Logger LOG = LoggerFactory.getLogger(ValueBindingTabChangeListener.class);
  private String type;
  private ValueBinding valueBinding;

  public ValueBindingTabChangeListener() {
  }

  public ValueBindingTabChangeListener(String type, ValueBinding valueBinding) {
    this.type = type;
    this.valueBinding = valueBinding;
  }

  public void processTabChange(TabChangeEvent tabChangeEvent) {
    TabChangeListener handler = getTabChangeListener();
    if (handler != null) {
      handler.processTabChange(tabChangeEvent);
    } else {
      LOG.error("Ignoring TabChangeEvent. No TabChangeListener found.");
    }
  }

  public Object saveState(FacesContext context) {
    Object[] state = new Object[2];
    state[0] = UIComponentBase.saveAttachedState(context, valueBinding);
    state[1] = type;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    valueBinding = (ValueBinding) UIComponentBase.restoreAttachedState(context, values[0]);
    type = (String) values[1];

  }

  public boolean isTransient() {
    return false;
  }

  public void setTransient(boolean newTransientValue) {
    // ignore
  }

  private TabChangeListener getTabChangeListener() {
    TabChangeListener handler = null;
    if (valueBinding != null) {
      Object obj = valueBinding.getValue(FacesContext.getCurrentInstance());
      if (obj != null && obj instanceof TabChangeListener) {
        handler = (TabChangeListener) obj;
      }
    }
    if (handler == null && type != null) {
      handler = createTabChangeListener(type);
      if (handler != null && valueBinding != null) {
        valueBinding.setValue(FacesContext.getCurrentInstance(), handler);
      }
    }
    return handler;
  }

  private TabChangeListener createTabChangeListener(String className) {
    try {
      Class clazz = getClass().getClassLoader().loadClass(className);
      return ((TabChangeListener) clazz.newInstance());
    } catch (Exception e) {
      LOG.error("", e);
    }
    return null;
  }

}
