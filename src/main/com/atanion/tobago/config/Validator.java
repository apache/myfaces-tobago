/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 31.03.2004 11:35:55.
 * $Id$
 */
package com.atanion.tobago.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import java.util.List;

public class Validator {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Validator.class);

// ///////////////////////////////////////////// attribute

  private String validatorId;

  private String validatorClass;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  protected static void addValidatorsToApplication(
      List validators, Application application) {
    ClassLoader classLoader = Validator.class.getClassLoader();
    for (int i = 0; i < validators.size(); i++) {
      Validator validator = (Validator) validators.get(i);
      application.addValidator(
          validator.getValidatorId(),
          validator.getValidatorClass());
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getValidatorId() {
    return validatorId;
  }

  public void setValidatorId(String validatorId) {
    this.validatorId = validatorId;
  }

  public String getValidatorClass() {
    return validatorClass;
  }

  public void setValidatorClass(String validatorClass) {
    this.validatorClass = validatorClass;
  }
}
