/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * This class works like the Java resource bundle mechanism for a named resource bundle
 * and adds the functionality of the tobago themes and also supports XML properties files.
 * This class might be used in the faces-config.xml as an alternative to the tc:loadBundle tag.
 *
 * @since 1.5.0
 */
public class TobagoBundle extends ResourceBundle {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoBundle.class);

  private String bundleName;

  public TobagoBundle(String bundleName) {
    this.bundleName = bundleName;
  }

  protected Object handleGetObject(String key) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Searching for '{}' in bundle '{}'", key, bundleName);
    }
    FacesContext facesContext = FacesContext.getCurrentInstance();
    return ResourceManagerUtils.getProperty(facesContext, bundleName, key);
  }

  public Enumeration<String> getKeys() {
    return Collections.enumeration(Collections.<String>emptyList());
  }

  public String getBundleName() {
    return bundleName;
  }
}
