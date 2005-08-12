/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.10.2004 11:51:00.
 * $Id$
 */
package org.apache.myfaces.tobago.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
      InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
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
