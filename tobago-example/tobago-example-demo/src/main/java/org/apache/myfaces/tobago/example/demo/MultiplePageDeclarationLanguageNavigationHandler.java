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

import org.apache.commons.lang.StringUtils;
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

  public MultiplePageDeclarationLanguageNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {

    String original = outcome;
    
    if (outcome != null) {
      PageDeclarationLanguageBean bean = (PageDeclarationLanguageBean)
          VariableResolverUtils.resolveVariable(facesContext, "pageDeclarationLanguage");

      for (PageDeclarationLanguage renderTechnology : PageDeclarationLanguage.values()) {
        if (outcome.endsWith(renderTechnology.getExtension())) {
          outcome = outcome.substring(0, outcome.lastIndexOf(renderTechnology.getExtension()));
        }
      }

      if (pageExists(facesContext, outcome, bean.getLanguage().getExtension())) {
        outcome = outcome + bean.getLanguage().getExtension();
      } else {
        for (PageDeclarationLanguage renderTechnology : PageDeclarationLanguage.values()) {
          if (pageExists(facesContext, outcome, renderTechnology.getExtension())) {
            bean.setLanguage(renderTechnology);
            outcome = outcome + bean.getLanguage().getExtension();
            break;
          }
        }
      }
    }

    LOG.info("Original outcome='"+  original + "', adjusted outcome = '"+ outcome +"'");

    if (StringUtils.startsWith(outcome, "/content/")) {
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      UIViewRoot viewRoot = viewHandler.createView(facesContext, outcome);
      facesContext.setViewRoot(viewRoot);
      final ExternalContext externalContext = facesContext.getExternalContext();
      try {
        externalContext.redirect(
            externalContext.encodeRedirectURL("/faces" + outcome, Collections.<String, List<String>>emptyMap()));
      } catch (IOException e) {
        // not nice?
        facesContext.renderResponse();
      }
    } else {
      navigationHandler.handleNavigation(facesContext, fromAction, outcome);
    }
  }

  private boolean pageExists(FacesContext facesContext, String outcome, String extension) {
    if (StringUtils.isEmpty(outcome)) {
      return false;
    }
    String path = ResourceManagerUtils.getImageWithPath(facesContext, outcome.substring(1) + extension, true);
    return path != null;
  }
}
