package org.apache.myfaces.tobago.bean;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @deprecated
 */
@Deprecated
public class ResourceMap extends Properties {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceMap.class);
  private static final long serialVersionUID = -6696019120255349519L;

  public ResourceMap() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("creating ResourceMap");
    }
  }

  public void setFilename(String filename) {
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
    if (LOG.isDebugEnabled()) {
      LOG.debug("size() = \"" + size() + "\"");
      for (Object x : keySet()) {
        LOG.debug("{}", x);
      }
    }
  }

  // setFilename() is never called with myfaces implementation,
  // because we implement Map. This hotfix enables filename setting via put().

  @Override
  public Object put(Object key, Object value) {
    if ("filename".equals(key)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("put(\"filename\", \"" + value + "\")");
      }
      setFilename(value.toString());
    }
    return super.put(key, value);
  }

  @Override
  public Object get(Object key) {
    Object value = super.get(key);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Query value for key='" + key + "' -> '" + value + "'");
    }
    if (value == null) {
      LOG.warn("Unknown value for key='" + key + "'");
    }
    return value;
  }
}
