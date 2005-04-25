/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.10.2004 11:51:00.
 * $Id$
 */
package com.atanion.tobago.bean;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import com.atanion.util.Resource;

public class ResourceMap extends Properties {

  private static final Log LOG = LogFactory.getLog(ResourceMap.class);

  public ResourceMap() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("creating ResourceMap");
    }
  }

  public void setFilename(String filename) throws IOException {
//    String filename = "de/inexso/erm/ui/image.properties";
    if (LOG.isDebugEnabled()) {
      LOG.debug("filename = '" + filename + "'");
    }
    try {
      InputStream is = Resource.getInputStream(filename);
      if (is == null) {
        LOG.error("Cannot load resource map from file: " + filename);
      }
      load(is);
    } catch (IOException e) {
      LOG.error("Cannot load resource map from file: " + filename, e);
    }
    for (Object x : keySet()) {
      LOG.debug(x);
    }
  }

  public Object get(Object key) {
    Object value = super.get(key);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Unknown value for key='" + key + "'");
    }
    if (value == null) {
      LOG.warn("Unknown value for key='" + key + "'");
    }
    return value;
  }
}
