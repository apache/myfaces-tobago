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

import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @deprecated since 1.5.0. Has been moved to internal package
 */
@Deprecated
public class ResourceManagerFactory {

  /**
   * @deprecated has been moved to internal package
   */
  @Deprecated
  public static final String RESOURCE_MANAGER
      = org.apache.myfaces.tobago.internal.context.ResourceManagerFactory.RESOURCE_MANAGER;

  private ResourceManagerFactory() {
  }

  /**
   * @deprecated has been moved to internal package
   */
  @Deprecated
  public static ResourceManager getResourceManager(FacesContext facesContext) {
    return org.apache.myfaces.tobago.internal.context.ResourceManagerFactory.getResourceManager(facesContext);
  }

  /**
   * @deprecated has been moved to internal package
   */
  @Deprecated
  public static ResourceManager getResourceManager(ServletContext servletContext) {
    return org.apache.myfaces.tobago.internal.context.ResourceManagerFactory.getResourceManager(servletContext);
  }

  /**
   * @deprecated has been moved to internal package
   */
  @Deprecated
  public static void init(ServletContext servletContext, TobagoConfigImpl tobagoConfig) throws ServletException {
    org.apache.myfaces.tobago.internal.context.ResourceManagerFactory.init(servletContext, tobagoConfig);
  }

  /**
   * @deprecated has been moved to internal package
   */
  @Deprecated
  public static void release(ServletContext servletContext) {
    org.apache.myfaces.tobago.internal.context.ResourceManagerFactory.release(servletContext);
  }
}
