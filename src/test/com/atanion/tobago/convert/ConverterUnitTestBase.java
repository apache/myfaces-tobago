/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 02.04.2004 16:36:30.
 * $Id$
 */
package com.atanion.tobago.convert;

import junit.framework.TestCase;

import com.atanion.tobago.context.MockExternalServletContext;
import com.atanion.tobago.context.TobagoContext;

import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class ConverterUnitTestBase extends TestCase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  protected Application application;

  protected FacesContext facesContext;

// ///////////////////////////////////////////// constructor

  public ConverterUnitTestBase(String reference) {
    super(reference);
  }

// ///////////////////////////////////////////// code

  protected void setUp() throws Exception {
    super.setUp();

    // faces-ri-config.xml
//    InputStream[] configStreams = new InputStream[1];
//    configStreams[0] =
//        getClass().getClassLoader().getResourceAsStream(
//            "com/atanion/tobago/faces-ri-config.xml");
//    FacesConfigParser facesConfigParser = new FacesConfigParser();
//    String url = getClass().getClassLoader().getResource(
//        "java/dtd/web-facesconfig_1_0.dtd").toString();
//    facesConfigParser.init(configStreams, null);
//    configStreams[0].close();

//    ApplicationFactory factory = (ApplicationFactory)
//        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
//    application = factory.getApplication();
    ExternalContext externalContext = new MockExternalServletContext();
    facesContext = new TobagoContext(externalContext, null);
    facesContext.setViewRoot(new UIViewRoot());
  }

// ///////////////////////////////////////////// bean getter + setter

}
