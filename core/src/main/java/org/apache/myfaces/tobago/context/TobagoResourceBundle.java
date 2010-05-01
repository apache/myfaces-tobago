package org.apache.myfaces.tobago.context;

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

import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

/*
 * User: weber
 * Date: Jun 14, 2005
 * Time: 2:24:44 PM
 */
public class TobagoResourceBundle extends ResourceBundle {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoResourceBundle.class);

  protected Object handleGetObject(String key) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("search for \"{}\"", key);
    }
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResourceManager resourceManager
        = ResourceManagerFactory.getResourceManager(facesContext);
    return resourceManager.getProperty(facesContext.getViewRoot(), "tobago", key);
  }

  public Enumeration<String> getKeys() {
    return Collections.enumeration(Collections.EMPTY_LIST);
  }
}
