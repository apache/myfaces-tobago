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

import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractUIMediator extends AbstractUIPanelBase implements LayoutComponent {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIMediator.class);

  private String var;

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    super.encodeBegin(facesContext);
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().put(var, this);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
    super.encodeEnd(facesContext);
  }

  @Override
  public void broadcast(final FacesEvent event) throws AbortProcessingException {
    final Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
    requestMap.put(var, this);
    try {
      super.broadcast(event);
    } finally {
      requestMap.remove(var);
    }
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] state = new Object[2];
    state[0] = super.saveState(context);
    state[1] = var;
    return state;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    var = (String) values[1];
  }

  public String getVar() {
    return var;
  }

  public void setVar(final String var) {
    this.var = var;
  }
}
