/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 09:01:22.
 * Id: $
 */
package com.atanion.tobago.config;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.FactoryFinder;

public class Factory {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Factory.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public void setApplicationFactory(String applicationFactory) {
    LOG.debug("setApplicationFactory to " + applicationFactory);
    FactoryFinder.setFactory(
        FactoryFinder.APPLICATION_FACTORY, applicationFactory);
  }

  public void setFacesContextFactory(String facesContextFactory) {
    LOG.debug("setFacesContextFactory to " + facesContextFactory);
    FactoryFinder.setFactory(
        FactoryFinder.FACES_CONTEXT_FACTORY, facesContextFactory);
  }

  public void setLifecycleFactory(String lifecycleFactory) {
    LOG.debug("setLifecycleFactory to " + lifecycleFactory);
    FactoryFinder.setFactory(
        FactoryFinder.LIFECYCLE_FACTORY, lifecycleFactory);
  }

  public void setRenderKitFactory(String renderKitFactory) {
    LOG.debug("setRenderKitFactory to " + renderKitFactory);
    FactoryFinder.setFactory(
        FactoryFinder.RENDER_KIT_FACTORY, renderKitFactory);
  }

}
