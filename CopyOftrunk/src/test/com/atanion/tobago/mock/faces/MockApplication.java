/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 10:30:12.
 * $Id: MockApplication.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package com.atanion.tobago.mock.faces;

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
import java.util.Map;
import java.util.HashMap;

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

