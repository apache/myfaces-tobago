package org.apache.myfaces.tobago.example.demo;

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
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

public class MultiplePageDeclarationLanguageNavigationHandler extends NavigationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MultiplePageDeclarationLanguageNavigationHandler.class);

  private NavigationHandler navigationHandler;

  public MultiplePageDeclarationLanguageNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {

    LOG.info("outcome='"+  outcome + "'");

    if (outcome != null) {
      PageDeclarationLanguageBean bean = (PageDeclarationLanguageBean)
          VariableResolverUtils.resolveVariable(facesContext, "pageDeclarationLanguage");

      for (PageDeclarationLanguage renderTechnologie : PageDeclarationLanguage.values()) {
        if (outcome.endsWith(renderTechnologie.getExtension())) {
          outcome = outcome.substring(0, outcome.lastIndexOf(renderTechnologie.getExtension()));
        }
      }

      if (pageExists(facesContext, outcome, bean.getLanguage().getExtension())) {
        outcome = outcome + bean.getLanguage().getExtension();
      } else {
        for (PageDeclarationLanguage renderTechnologie : PageDeclarationLanguage.values()) {
          if (pageExists(facesContext, outcome, renderTechnologie.getExtension())) {
            bean.setLanguage(renderTechnologie);
            outcome = outcome + bean.getLanguage().getExtension();
            break;
          }
        }
      }
    }

    LOG.info("outcome='"+  outcome + "'");

    navigationHandler.handleNavigation(facesContext, fromAction, outcome);
  }

  private boolean pageExists(FacesContext facesContext, String outcome, String extension) {
    String path = ResourceManagerUtil.getImageWithPath(facesContext, outcome.substring(1) + extension, true);
    return path != null;
  }
}
