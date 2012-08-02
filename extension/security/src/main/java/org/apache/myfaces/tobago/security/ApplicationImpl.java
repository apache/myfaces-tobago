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

package org.apache.myfaces.tobago.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.event.ActionListener;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.component.UIComponent;
import javax.faces.FacesException;
import javax.faces.validator.Validator;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import java.util.Locale;
import java.util.Iterator;
import java.util.Collection;

public class ApplicationImpl extends Application {
  private static final Log LOG = LogFactory.getLog(ApplicationImpl.class);

  private Application application;

  public ApplicationImpl(Application application) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Hiding Application");
    }
    this.application = application; 
  }

  public ActionListener getActionListener() {
    return application.getActionListener();
  }

  public void setActionListener(ActionListener actionListener) {
    application.setActionListener(actionListener);
  }

  public Locale getDefaultLocale() {
    return application.getDefaultLocale();
  }

  public void setDefaultLocale(Locale locale) {
    application.setDefaultLocale(locale);
  }

  public String getDefaultRenderKitId() {
    return application.getDefaultRenderKitId();
  }

  public void setDefaultRenderKitId(String s) {
    application.setDefaultRenderKitId(s);
  }

  public String getMessageBundle() {
    return application.getMessageBundle();
  }

  public void setMessageBundle(String s) {
    application.setMessageBundle(s);
  }

  public NavigationHandler getNavigationHandler() {
    return application.getNavigationHandler();
  }

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    application.setNavigationHandler(navigationHandler);
  }

  public PropertyResolver getPropertyResolver() {
    return application.getPropertyResolver();
  }

  public void setPropertyResolver(PropertyResolver propertyResolver) {
    application.setPropertyResolver(propertyResolver);
  }

  public VariableResolver getVariableResolver() {
    return application.getVariableResolver();
  }

  public void setVariableResolver(VariableResolver variableResolver) {
    application.setVariableResolver(variableResolver);
  }

  public ViewHandler getViewHandler() {
    return application.getViewHandler();
  }

  public void setViewHandler(ViewHandler viewHandler) {
    application.setViewHandler(viewHandler);
  }

  public StateManager getStateManager() {
    return application.getStateManager();
  }

  public void setStateManager(StateManager stateManager) {
    application.setStateManager(stateManager);
  }

  public void addComponent(String s, String s1) {
    application.addComponent(s, s1);
  }

  public UIComponent createComponent(String s) throws FacesException {
    return application.createComponent(s);
  }

  public UIComponent createComponent(ValueBinding valueBinding, FacesContext facesContext, String s) throws
      FacesException {
    return application.createComponent(valueBinding, facesContext, s);
  }

  public Iterator getComponentTypes() {
    return application.getComponentTypes();
  }

  public void addConverter(String s, String s1) {
    application.addConverter(s, s1);
  }

  public void addConverter(Class aClass, String s) {
    application.addConverter(aClass, s);
  }

  public Converter createConverter(String s) {
    return application.createConverter(s);
  }

  public Converter createConverter(Class aClass) {
    return application.createConverter(aClass);
  }

  public Iterator getConverterIds() {
    return application.getConverterIds();
  }

  public Iterator getConverterTypes() {
    return application.getConverterTypes();
  }

  public MethodBinding createMethodBinding(String s, Class[] classes) throws ReferenceSyntaxException {
    return new CheckAuthorisationMethodBinding(application.createMethodBinding(s, classes));
  }

  public Iterator getSupportedLocales() {
    return application.getSupportedLocales();
  }

  public void setSupportedLocales(Collection collection) {
    application.setSupportedLocales(collection);
  }

  public void addValidator(String s, String s1) {
    application.addValidator(s, s1);
  }

  public Validator createValidator(String s) throws FacesException {
    return application.createValidator(s);
  }

  public Iterator getValidatorIds() {
    return application.getValidatorIds();
  }

  public ValueBinding createValueBinding(String s) throws ReferenceSyntaxException {
    return application.createValueBinding(s);
  }


}
