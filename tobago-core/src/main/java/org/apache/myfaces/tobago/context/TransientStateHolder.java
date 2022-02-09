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

package org.apache.myfaces.tobago.context;


import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class TransientStateHolder implements StateHolder, Serializable {

  private static final long serialVersionUID = -5260593843885016768L;

  private transient Object object;

  public TransientStateHolder() {
  }

  public TransientStateHolder(final Object object) {
    this.object = object;
  }

  public Object saveState(final FacesContext context) {
    // do nothing
    return null;
  }

  public void put(final Object objectParameter) {
    this.object = objectParameter;
  }

  public boolean isEmpty() {
    return object == null;
  }

  public Object get() {
    return object;
  }

  public void restoreState(final FacesContext context, final Object state) {
    // do nothing
  }

  public boolean isTransient() {
    return true;
  }

  public void setTransient(final boolean newTransientValue) {
    // do nothing
  }

}
