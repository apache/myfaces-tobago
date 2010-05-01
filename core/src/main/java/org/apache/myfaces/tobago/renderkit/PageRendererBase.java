package org.apache.myfaces.tobago.renderkit;

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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.layout.Box;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class PageRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(PageRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    if (component instanceof AbstractUIPage) {
      AbstractUIPage page = (AbstractUIPage) component;

      String actionIdName = page.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "form-action";
      String newActionId = (String) facesContext.getExternalContext().getRequestParameterMap().get(actionIdName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("action = " + newActionId);
      }
      page.setActionId(newActionId);

      try {
        String actionPositionName = page.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "action-position";
        String actionPositionString = (String)
            facesContext.getExternalContext().getRequestParameterMap().get(actionPositionName);
        if (LOG.isDebugEnabled()) {
          LOG.debug("actionPosition='" + actionPositionString + "'");
        }
        if (StringUtils.isNotEmpty(actionPositionString)) {
          Box actionPosition = new Box(actionPositionString);
          page.setActionPosition(actionPosition);
        } else {
          page.setActionPosition(null);
        }
      } catch (Exception e) {
        LOG.warn("Can't analyse parameter for action-position", e);
      }
    }
  }
}
