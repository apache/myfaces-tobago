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

package org.apache.myfaces.tobago.internal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import java.lang.invoke.MethodHandles;

/**
 * The code is this class is to help the {@link AuthorizationHelper} in the case of CDI. In case of no CDI, this class
 * will never loaded to prevent problems the import of class of the package javax.enterprise.inject.spi.
 */
class AuthorizationHelperCdi {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private BeanManager beanManager;

  AuthorizationHelperCdi() {

    try {
      // XXX this is easier with CDI 1.1
      // beanManager = CDI.context().getBeanManager();
      final InitialContext context = new InitialContext();
      beanManager = (BeanManager) context.lookup("java:comp/BeanManager");
    } catch (final Exception exception) {
      LOG.warn("Can't obtain 'java:comp/BeanManager'");
    }

    if (beanManager == null) {
      // this works with Jetty 9
      beanManager = (BeanManager)
          FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(BeanManager.class.getName());
    }

    LOG.info("Using bean manager: '{}'", beanManager);
  }

  Object getObject(String beanString) {
    Object bean = null;
    for (final Bean<?> entry : beanManager.getBeans(beanString)) {
      if (bean == null) {
        bean = entry;
      } else {
        LOG.warn("Bean name ambiguous: '{}'", beanString);
      }
    }
    return bean;
  }

  Class getBeanClass(Object bean) {
    return ((Bean) bean).getBeanClass();
  }

  boolean hasBeanManager() {
    return beanManager != null;
  }
}
