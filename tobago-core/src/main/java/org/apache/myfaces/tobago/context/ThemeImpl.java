package org.apache.myfaces.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class ThemeImpl implements Theme, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Log LOG = LogFactory.getLog(ThemeImpl.class);

  private String name;

  private String deprecatedName;

  private String resourcePath;

  private ThemeImpl fallback;

  private String fallbackName;

  private List<Theme> fallbackList;

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return name; // FIXME
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDeprecatedName() {
    return deprecatedName;
  }

  public void setDeprecatedName(String deprecatedName) {
    this.deprecatedName = deprecatedName;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public ThemeImpl getFallback() {
    return fallback;
  }

  public void setFallback(ThemeImpl fallback) {
    this.fallback = fallback;
  }

  public String getFallbackName() {
    return fallbackName;
  }

  public void setFallbackName(String fallbackName) {
    this.fallbackName = fallbackName;
  }

  public List<Theme> getFallbackList() {
    return fallbackList;
  }

  public void resolveFallbacks() {
    fallbackList = new ArrayList<Theme>();
    ThemeImpl actual = this;
    while (actual != null) {
      fallbackList.add(actual);
      actual = actual.getFallback();
    }
    fallbackList = Collections.unmodifiableList(fallbackList);
    if (LOG.isDebugEnabled()) {
      for (Theme theme : fallbackList) {
        LOG.debug("fallbackList: " + theme.getName());
      }
    }
  }

  public String toString() {
//    LOG.warn("Should not be called!", new Exception());
    return name;
  }
}
