/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 09:26:24.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.UIPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class PageRendererBase extends RendererBase {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(PageRendererBase.class);

// ----------------------------------------------------------- business methods

  public void decode(FacesContext facesContext, UIComponent component) {
    UIPage page = (UIPage) component;
    String name = page.getClientId(facesContext)
        + SUBCOMPONENT_SEP + "form-action";
    String newActionId = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(name);
    page.setActionId(newActionId);
  }
}

