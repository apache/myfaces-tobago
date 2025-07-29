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

package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MultiplePageDeclarationLanguageNavigationHandler extends NavigationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MultiplePageDeclarationLanguageNavigationHandler.class);

  private NavigationHandler navigationHandler;

  public MultiplePageDeclarationLanguageNavigationHandler(final NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void handleNavigation(
      final FacesContext facesContext, final String fromAction, final String outcomeParameter) {

    String outcome = outcomeParameter;

    if (outcome != null) {
      final PageDeclarationLanguageBean bean = (PageDeclarationLanguageBean)
          VariableResolverUtils.resolveVariable(facesContext, "pageDeclarationLanguage");

      for (final PageDeclarationLanguage renderTechnology : PageDeclarationLanguage.values()) {
        if (outcome.endsWith(renderTechnology.getExtension())) {
          outcome = outcome.substring(0, outcome.lastIndexOf(renderTechnology.getExtension()));
        }
      }

      if (pageExists(facesContext, outcome, bean.getLanguage().getExtension())) {
        outcome = outcome + bean.getLanguage().getExtension();
      } else {
        for (final PageDeclarationLanguage renderTechnology : PageDeclarationLanguage.values()) {
          if (pageExists(facesContext, outcome, renderTechnology.getExtension())) {
            bean.setLanguage(renderTechnology);
            outcome = outcome + bean.getLanguage().getExtension();
            break;
          }
        }
      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Original outcome='" + outcomeParameter + "', adjusted outcome = '" + outcome + "'");
    }

    if (StringUtils.startsWith(outcome, "/content/")) {
      final ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      final UIViewRoot viewRoot = viewHandler.createView(facesContext, outcome);
      facesContext.setViewRoot(viewRoot);
      final ExternalContext externalContext = facesContext.getExternalContext();
      try {
        final String baseUrl = externalContext.getRequestContextPath() + "/faces" + outcome;
/*
        Change this, when changing the <url-pattern> in web.xml
        final String baseUrl = externalContext.getRequestContextPath() + outcome.replaceAll("xhtml", "jsf");
*/
        externalContext.redirect(
            externalContext.encodeRedirectURL(baseUrl, Collections.<String, List<String>>emptyMap()));

      } catch (final IOException e) {
        // not nice?
        facesContext.renderResponse();
      }
    } else {
      navigationHandler.handleNavigation(facesContext, fromAction, outcome);
    }
  }

  private boolean pageExists(final FacesContext facesContext, final String outcome, final String extension) {
    if (StringUtils.isEmpty(outcome)) {
      return false;
    }
    final String path = ResourceManagerUtils.getFile(facesContext, outcome.substring(1), extension, true);
    return path != null;
  }
}
