package org.apache.myfaces.tobago.context;

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

import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ResourceManagerFactory {

  public static final String RESOURCE_MANAGER
      = "org.apache.myfaces.tobago.context.ResourceManager";

  private ResourceManagerFactory() {
  }

  private static boolean initialized;

  public static ResourceManager getResourceManager(FacesContext facesContext) {
    assert initialized;
    return (ResourceManager) facesContext.getExternalContext()
        .getApplicationMap().get(RESOURCE_MANAGER);
  }

  public static ResourceManager getResourceManager(ServletContext servletContext) {
    assert initialized;
    return (ResourceManager) servletContext.getAttribute(RESOURCE_MANAGER);
  }

  public static ResourceManagerImpl init(
      ServletContext servletContext, TobagoConfig tobagoConfig)
      throws ServletException {
    assert !initialized;
    ResourceManagerImpl resourceManager = new ResourceManagerImpl();

    ThemeBuilder themeBuilder = new ThemeBuilder();
    ResourceLocator resourceLocator = new ResourceLocator(
        servletContext, resourceManager, themeBuilder,
        tobagoConfig.isLoadThemesFromClasspath());
    resourceLocator.init();
    tobagoConfig.setAvailableThemes(themeBuilder.resolveThemes());

    servletContext.setAttribute(RESOURCE_MANAGER, resourceManager);

    initialized = true;

    return resourceManager;
  }

  public static void release(ServletContext servletContext) {
    assert initialized;
    initialized = false;
    servletContext.removeAttribute(RESOURCE_MANAGER);
  }
}
