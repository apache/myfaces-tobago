/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.06.2003 20:08:38.
 * $Id$
 */
package com.atanion.tobago.application;

import com.atanion.tobago.el.ElUtil;
import com.atanion.tobago.el.MethodBindingImpl;
import com.atanion.tobago.el.ValueBindingImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// fixme: must be thread safe !!!

public class ApplicationImpl extends Application {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ApplicationImpl.class);

// ///////////////////////////////////////////// attribute

  private ActionListener actionListener;
  private NavigationHandler navigationHandler;
  private PropertyResolver propertyResolver;
  private StateManager stateManager;
  private VariableResolver variableResolver;
  private ViewHandler viewHandler;
  private Locale defaultLocale;
  private List supportedLocales;

  private Map converterIdMap;
  private Map converterTypeMap;
  private Map componentTypeMap;
  private Map validatorIdMap;

// ///////////////////////////////////////////// constructor

  public ApplicationImpl() {
    LOG.info("stack trace", new Exception());
    converterIdMap = new HashMap();
    converterTypeMap = new HashMap();
    componentTypeMap = new HashMap();
    validatorIdMap = new HashMap();
  }

// ///////////////////////////////////////////// code

  public ValueBinding createValueBinding(String reference)
      throws ReferenceSyntaxException {
    reference = ElUtil.clipReferenceBackets(reference);
    ValueBindingImpl binding = new ValueBindingImpl(reference);
    return binding;
  }

  public MethodBinding createMethodBinding(String reference, Class params[])
      throws ReferenceSyntaxException {
    MethodBinding binding = new MethodBindingImpl(this, reference, params);
    return binding;
  }

// ///////////////////////////////////////////// converter

  public void addConverter(String converterId, String converterClass) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("converter-id = '" + converterId + "' "
          + "converter-class = '" + converterClass + "'");    }
    converterIdMap.put(converterId, converterClass);
  }

  public void addConverter(Class targetClass, String converterClass) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("converter-for-class = '" + targetClass+ "' "
          + "converter-class = '" + converterClass + "'");
    }
    converterTypeMap.put(targetClass, converterClass);
  }

  public Converter createConverter(String converterId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("converterId = '" + converterId + "'");
    }
    String className = (String) converterIdMap.get(converterId);
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      Converter converter
          = (Converter) classLoader.loadClass(className).newInstance();
      return converter;
    } catch (Throwable e) {
      String error = "Cannot create converter for id='" + converterId + "'" +
          " className='" + className + "'";
      LOG.error(error, e);
      throw new FacesException(error, e);
    }
  }

  public Converter createConverter(Class targetClass) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("targetClass = '" + targetClass.getName() + "'");
    }
    String className = (String) converterTypeMap.get(targetClass);
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      Converter converter
          = (Converter) classLoader.loadClass(className).newInstance();
      return converter;
    } catch (Throwable e) {
      String error = "Cannot create converter for class='" + targetClass + "'" +
          " className='" + className + "'";
      LOG.error(error, e);
      throw new FacesException(error, e);
    }
  }

  public Iterator getConverterIds() {
    return converterIdMap.keySet().iterator();
  }

  public Iterator getConverterTypes() {
    return converterTypeMap.keySet().iterator();
  }

// ///////////////////////////////////////////// converter

  public UIComponent createComponent(String componentType)
      throws FacesException {
    UIComponent component;
    try {
      Class componentClass = (Class) componentTypeMap.get(componentType);
      component = (UIComponent)componentClass.newInstance();
    } catch (Exception e) {
      String text = "componentType: '" + componentType + "'";
      LOG.error(text, e);
      throw new FacesException(text, e);
    }

    return component;
  }

  public void addComponent(String componentType, String componentClass) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("componentType = '" + componentType + "' "
          + "componentClass = '" + componentClass + "'");
    }
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      componentTypeMap.put(componentType, classLoader.loadClass(componentClass));
    } catch (ClassNotFoundException e) {
      String text = "Cannot create component class"
          + " componentType = '" + componentType + "'"
          + " componentClass = '" + componentClass + "'"
          + " message = '" + e.getMessage() + "'";
      LOG.error(text, e);
      throw new RuntimeException(text, e);
    }
  }

  public UIComponent createComponent(
      ValueBinding componentBinding, FacesContext facesContext,
      String componentType)
      throws FacesException {

    Object result = componentBinding.getValue(facesContext);
    if (result == null || !(result instanceof UIComponent)) {
      result = createComponent(componentType);
      componentBinding.setValue(facesContext, result);
    }
    return (UIComponent) result;
  }

  public Iterator getComponentTypes() {
    return componentTypeMap.keySet().iterator();
  }

// ///////////////////////////////////////////// validator

  public void addValidator(String validatorId, String validatorClass) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("validatorId = '" + validatorId + "' "
          + "validatorClass = '" + validatorClass + "'");
    }
    validatorIdMap.put(validatorId, validatorClass);
  }

  public Validator createValidator(String validatorId) throws FacesException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("validatorId = '" + validatorId + "'");
    }
    String className = (String) validatorIdMap.get(validatorId);
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      Validator validator
          = (Validator) classLoader.loadClass(className).newInstance();
      return validator;
    } catch (Throwable e) {
      String error = "Cannot create validator for id='" + validatorId + "'" +
          " className='" + className + "'";
      LOG.error(error, e);
      throw new FacesException(error, e);
    }
  }

  public Iterator getValidatorIds() {
    return validatorIdMap.keySet().iterator();
  }

// ///////////////////////////////////////////// locale

  public Locale getDefaultLocale() {
    return defaultLocale;
  }

  public void setDefaultLocale(Locale defaultLocale) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("defaultLocale = '" + defaultLocale + "'");
    }
    this.defaultLocale = defaultLocale;
  }

  public Iterator getSupportedLocales() {
    if (supportedLocales != null) {
      return supportedLocales.iterator();
    } else {
      return Collections.EMPTY_LIST.iterator();
    }
  }

  public void setSupportedLocales(Collection supportedLocales) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("supportedLocales = '" + supportedLocales + "'");
    }
    this.supportedLocales = new ArrayList(supportedLocales);
  }

// ///////////////////////////////////////////// bean getter + setter

  public ActionListener getActionListener() {
    return actionListener;
  }

  public void setActionListener(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  public NavigationHandler getNavigationHandler() {
    return navigationHandler;
  }

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public PropertyResolver getPropertyResolver() {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return propertyResolver;
  }

  public void setPropertyResolver(PropertyResolver propertyResolver) {
    this.propertyResolver = propertyResolver;
  }

  public StateManager getStateManager() {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return stateManager;
  }

  public void setStateManager(StateManager stateManager) {
    this.stateManager = stateManager;
  }

  public VariableResolver getVariableResolver() {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return variableResolver;
  }

  public void setVariableResolver(VariableResolver variableResolver) {
    this.variableResolver = variableResolver;
  }

  public ViewHandler getViewHandler() {
    return viewHandler;
  }

  // fixme: jsfbeta must throw exception when ever used after first request, see spec...
  public void setViewHandler(ViewHandler viewHandler)   {
    this.viewHandler = viewHandler;
  }

// ///////////////////////////////////////////// not implemnted yet

  public void setMessageBundle(String s) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme: jsfbeta
  }

  public String getMessageBundle() {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme: jsfbeta
    return null;
  }

  public String getDefaultRenderKitId() {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme: jsf1.0
    return null;
  }

  public void setDefaultRenderKitId(String reference) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme: jsf1.0
  }

}
