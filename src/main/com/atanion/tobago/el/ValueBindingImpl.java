/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 11.06.2003 14:23:37.
 * Id: $
 */
package com.atanion.tobago.el;

import com.atanion.util.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ValueBindingImpl extends ValueBinding {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ValueBindingImpl.class);

// ///////////////////////////////////////////// attribute

  protected String reference;

// ///////////////////////////////////////////// constructor

  public ValueBindingImpl(String reference) {
    this.reference = reference;
  }

// ///////////////////////////////////////////// code

  public Object getValue(FacesContext facesContext)
      throws PropertyNotFoundException {
    if (reference == null) {
      throw new NullPointerException("expression missing");
    }
    Object object = getModelObject(facesContext, reference);
    String property = getPropertyReference(reference);
    Object value;
    if (property == null) {
      value = object;
    } else {
      try {
        value = BeanUtils.getProperty(object, property);
      } catch (Exception e) {
        throw new FacesException("reference = '" + reference + "' " +
            "property = '" + property + "' " +
            "object = '" + object + "' ", e);
      }
    }
//    Log.debug("getValue of '" + reference + "' is '" + value + "'");
    return value;
  }

  public void setValue(FacesContext facesContext, Object value)
      throws PropertyNotFoundException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("setValue of '" + reference + "' to '" + value +
          "' with type="
          + (value != null ? value.getClass().getName() : "null"));
    }

    if (reference == null) {
      throw new NullPointerException("reference missing");
    }
    Object object = getModelObject(facesContext, reference);
    String property = getPropertyReference(reference);
    if (property == null) {
      object = value;
      ((ServletContext) facesContext.getExternalContext().getContext()).setAttribute(
          reference, object);
    } else {
      try {

        BeanUtils.setProperty(object, property, value);
      } catch (Exception e) {
        FacesException facesException = new FacesException(
            "reference = '" + reference + "'\n" +
            "object = '" + object + "' \n" +
            "property = '" + property + "' \n" +
            "value = '" + value + "' "
            , e);
        LOG.error(facesException, e);
        throw facesException;
      }
    }
  }

  public boolean isReadOnly(FacesContext facesContext)
      throws PropertyNotFoundException {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return false;
  }

  public Class getType(FacesContext facesContext)
      throws PropertyNotFoundException {
    if (reference == null) {
      throw new NullPointerException("reference missing");
    }
    Object object = getModelObject(facesContext, reference);
    String property = getPropertyReference(reference);
    Class type;
    if (property == null) {
      type = object.getClass();
    } else {
      try {
// workaround: because in case of an array getPropertyType() returns the
// array type and _NOT_ the type of the element. I don't know why.
// todo: check if this is okay
        type = BeanUtils.getPropertyType(object, property);
        String lastPart = property.substring(property.lastIndexOf('.') + 1);
        while (type.isArray() && lastPart.indexOf('[') > -1) {
          type = type.getComponentType();
          lastPart = lastPart.substring(0, lastPart.lastIndexOf('['));
        }
// old code:        returnClass = PropertyUtils.getPropertyType(object, property);
      } catch (Exception e) {
        throw new FacesException("reference = '" + reference + "'", e);
      }
    }
    LOG.debug("getType of '" + reference + "' is '" + type + "'");
    return type;
  }

  public String getExpressionString() {
    return reference;
  }

  private static String getPropertyReference(String reference) {
    String property = null;
    int index = Math.min(reference.indexOf("."), reference.indexOf("["));
    if (index == -1) {
      index = Math.max(reference.indexOf("."), reference.indexOf("["));
    }
    if (index != -1) {
      if (reference.charAt(index) == '[') {
        property = reference.substring(index);
      } else {
        property = reference.substring(index + 1);
      }
    }
    return property;
  }

  private static Object getModelObject(FacesContext facesContext,
      String reference) throws FacesException {
    Object returnObject;
    String baseName;

    if (reference.indexOf(".") == -1 && reference.indexOf("[") == -1) {
      baseName = reference;
    } else {
      int index = Math.min(reference.indexOf("."), reference.indexOf("["));
      if (index == -1) {
        index = Math.max(reference.indexOf("."), reference.indexOf("["));
      }
      baseName = reference.substring(0, index);
    }
    returnObject = getObjectFromScope(facesContext, baseName);
    if (returnObject == null) {
      throw new FacesException("Named Object: '" + baseName + "' not found.");
    }

    return returnObject;
  }

  private static Object getObjectFromScope(FacesContext facesContext,
      String modelRef) {
    Object modelObj;
    modelObj
        = ((HttpServletRequest) facesContext.getExternalContext().getRequest())
        .getAttribute(modelRef);
    if (modelObj != null) {
      return modelObj;
    }
    modelObj
        = ((HttpSession) facesContext.getExternalContext().getSession(true))
        .getAttribute(modelRef);
    if (modelObj != null) {
      return modelObj;
    }
    modelObj
        = ((ServletContext) facesContext.getExternalContext().getContext())
        .getAttribute(modelRef);
    return modelObj;
  }

// ///////////////////////////////////////////// bean getter + setter


}
