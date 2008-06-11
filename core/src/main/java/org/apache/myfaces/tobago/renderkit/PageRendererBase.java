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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.AbstractUIPage;
import org.apache.myfaces.tobago.layout.Box;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class PageRendererBase extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(PageRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    if (component instanceof AbstractUIPage) {
      AbstractUIPage page = (AbstractUIPage) component;

      {
        String name = page.getClientId(facesContext) + SUBCOMPONENT_SEP + "form-action";
        String newActionId = (String) facesContext.getExternalContext().getRequestParameterMap().get(name);
        if (LOG.isDebugEnabled()) {
          LOG.debug("action = " + newActionId);
        }
        page.setActionId(newActionId);
      }

      try {
        String name = page.getClientId(facesContext) + SUBCOMPONENT_SEP + "action-position";
        String actionPositionString = (String) facesContext.getExternalContext().getRequestParameterMap().get(name);
        LOG.info("actionPosition='" + actionPositionString + "'");
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
