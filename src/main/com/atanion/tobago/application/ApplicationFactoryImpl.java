/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.06.2003 20:00:50.
 * $Id$
 */
package com.atanion.tobago.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

public class ApplicationFactoryImpl extends ApplicationFactory {


// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private Application application;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public Application getApplication() {
    if (application == null) {
      application = new ApplicationImpl();
    }
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }
}

