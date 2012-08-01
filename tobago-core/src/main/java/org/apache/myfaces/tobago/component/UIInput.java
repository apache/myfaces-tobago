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

package org.apache.myfaces.tobago.component;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @deprecated since 1.5.0. Please use {@link UIIn}
 */
@Deprecated
public interface UIInput extends StateHolder, EditableValueHolder {

  Map<String, Object> getAttributes();

  ValueBinding getValueBinding(String name);

  void setValueBinding(String name, ValueBinding binding);

  String getClientId(FacesContext context);

  String getFamily();

  String getId();

  void setId(String id);

  UIComponent getParent();

  void setParent(UIComponent parent);

  boolean isRendered();

  void setRendered(boolean rendered);

  String getRendererType();

  void setRendererType(String rendererType);

  boolean getRendersChildren();

  List<UIComponent> getChildren();

  int getChildCount();

  UIComponent findComponent(String expr);

  Map<String, UIComponent> getFacets();

  UIComponent getFacet(String name);

  Iterator<UIComponent> getFacetsAndChildren();

  void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException;

  void decode(FacesContext context);

  void encodeBegin(FacesContext context) throws IOException;

  void encodeChildren(FacesContext context) throws IOException;

  void encodeEnd(FacesContext context) throws IOException;

  void queueEvent(javax.faces.event.FacesEvent event);

  void processRestoreState(FacesContext context, Object state);

  void processDecodes(FacesContext context);

  void processValidators(FacesContext context);

  void processUpdates(FacesContext context);

  Object processSaveState(FacesContext context);

  void setSuggestMethod(MethodBinding suggestMethod);
}
