/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created Dec 16, 2002 at 3:42:32 PM.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.model.Color16;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;


public class UIColor16 extends UIInput {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UIColor16.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void updateModel(FacesContext facesContext) {
    if (ComponentUtil.mayUpdateModel(this)) {
      if (!isValid()) {
        return;
      }
      LOG.error("fixme: getValueRef() no longer implemented!"); //fixme
      String valueRef = null;//getValueRef();
      if (valueRef == null) {
        return;
      }
      try {
        ApplicationFactory factory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();
//        ValueBinding binding = application.getValueBinding(valueRef + ".RGBValue");
        LOG.error(
            "COMMENDTED!!! ValueBinding binding " +
            "= application.getValueBinding(valueRef + \".RGBValue\");"); // fixme jsfbeta
//        binding.setValue(facesContext, getValue());
        LOG.error("binding.setValue(facesContext, getValue());");
        setValue(null);
        return;
      } catch (FacesException e) {
        LOG.error("valueRef: " + valueRef, e);
        setValid(false);
        throw e;
      } catch (IllegalArgumentException e) {
        LOG.error("valueRef: " + valueRef, e);
        setValid(false);
        throw e;
      } catch (Exception e) {
        LOG.error("valueRef: " + valueRef, e);
        setValid(false);
        throw new FacesException(e);
      }
    }
  }

  protected Object checkValue(Object currentValue) {

    if (currentValue == null) {
      LOG.error("currentValue is null: '" + currentValue + "'");
      currentValue = emergencyValue();
    }

    if (!(currentValue instanceof Color16)) {
      LOG.error("currentValue is not valid: '" + currentValue + "'");
      LOG.error(
          "currentValue is not of type '"
          + Color16.class.getName() + "': '"
          + currentValue.getClass().getName() + "'");
      currentValue = emergencyValue();
    }

    return currentValue;
  }

  protected Object emergencyValue() {
    return new Color16(Color16.RED);
  }

// ///////////////////////////////////////////// bean getter + setter

}

