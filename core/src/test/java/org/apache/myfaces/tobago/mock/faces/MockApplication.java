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

package org.apache.myfaces.tobago.mock.faces;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class MockApplication extends Application {
// ----------------------------------------------------------------- attributes

  private ViewHandler viewHandler;
  private Map components = new HashMap();

  private PropertyResolver propertyResolver = null;

  private VariableResolver variableResolver = null;

// ----------------------------------------------------------- business methods

  public void addComponent(String componentType, String componentClass) {
    components.put(componentType, componentClass);
  }

  public void addConverter(Class aClass, String reference) {
  }

  public void addConverter(String reference, String reference1) {
  }

  public void addValidator(String reference, String reference1) {
  }

  public UIComponent createComponent(String componentType) throws FacesException {
    String componentClass = (String) components.get(componentType);
    try {
        Class clazz = Class.forName(componentClass);
        return ((UIComponent) clazz.newInstance());
    } catch (Exception e) {
        throw new FacesException("componentType=" + componentType, e);
    }
  }

  public UIComponent createComponent(ValueBinding valueBinding,
      FacesContext facesContext, String reference) throws FacesException {
   	throw new FacesException(new UnsupportedOperationException());
  }

  public Converter createConverter(Class aClass) {
    return null;
  }

  public Converter createConverter(String reference) {
    return null;
  }

  public MethodBinding createMethodBinding(String reference, Class[] classes)
      throws ReferenceSyntaxException {
    return null;
  }

  public Validator createValidator(String reference) throws FacesException {
    return null;
  }

  public ValueBinding createValueBinding(String reference){
  if (reference == null) {
      throw new NullPointerException();
  } else {
      return (new MockValueBinding(this, reference));
  }
  }

  public ActionListener getActionListener() {
    return null;
  }

  public Iterator getComponentTypes() {
    return null;
  }

  public Iterator getConverterIds() {
    return null;
  }

  public Iterator getConverterTypes() {
    return null;
  }

  public Locale getDefaultLocale() {
    return null;
  }

  public String getDefaultRenderKitId() {
    return null;
  }

  public String getMessageBundle() {
    return null;
  }

  public NavigationHandler getNavigationHandler() {
    return null;
  }

  public PropertyResolver getPropertyResolver() {
      if (propertyResolver == null) {
          propertyResolver = new MockPropertyResolver();
      }
      return (this.propertyResolver);
  }

  public StateManager getStateManager() {
    return null;
  }

  public Iterator getSupportedLocales() {
    return null;
  }

  public Iterator getValidatorIds() {
    return null;
  }

  public VariableResolver getVariableResolver() {
      if (variableResolver == null) {
          variableResolver = new MockVariableResolver();
      }
      return (this.variableResolver);
  }

  public void setActionListener(ActionListener actionListener) {
  }

  public void setDefaultLocale(Locale locale) {
  }

  public void setDefaultRenderKitId(String reference) {
  }

  public void setMessageBundle(String reference) {
  }

  public void setNavigationHandler(NavigationHandler navigationHandler) {
  }

  public void setStateManager(StateManager stateManager) {
  }

  public void setSupportedLocales(Collection collection) {
  }

// ------------------------------------------------------------ getter + setter

  public ViewHandler getViewHandler() {
    if (null == viewHandler) {
        viewHandler = new MockViewHandler();
    }
    return viewHandler;
  }

  public void setViewHandler(ViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  public void setPropertyResolver(PropertyResolver propertyResolver) {
      this.propertyResolver = propertyResolver;
  }

  public void setVariableResolver(VariableResolver variableResolver) {
      this.variableResolver = variableResolver;
  }
}

