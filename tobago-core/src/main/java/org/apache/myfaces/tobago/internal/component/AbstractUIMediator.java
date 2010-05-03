package org.apache.myfaces.tobago.internal.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.layout.LayoutComponent;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;

public abstract class AbstractUIMediator extends UIPanelBase implements LayoutComponent {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIMediator.class);

  private String var;

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    super.encodeBegin(facesContext);
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().put(var, this);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
    super.encodeEnd(facesContext);
  }

  @Override
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getExternalContext().getRequestMap().put(var, this);
    super.broadcast(event);
    facesContext.getExternalContext().getRequestMap().remove(var);
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] state = new Object[2];
    state[0] = super.saveState(context);
    state[1] = var;
    return state;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    var = (String) values[1];
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }
}
