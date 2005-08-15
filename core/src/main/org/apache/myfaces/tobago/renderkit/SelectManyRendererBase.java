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
  * Created 15.04.2003 at 10:17:34.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

public class SelectManyRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(
      SelectManyRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UISelectMany uiSelectMany = (UISelectMany) component;

    String newValue[] = ((ServletRequest) facesContext.getExternalContext()
        .getRequest())
        .getParameterValues(uiSelectMany.getClientId(facesContext));
    if (LOG.isDebugEnabled()) {
      LOG.debug("decode: key='" + component.getClientId(facesContext)
          + "' value='" + newValue + "'");
      LOG.debug("size ... '" + (newValue != null ? newValue.length : -1) + "'");
      if (newValue != null) {
        for (int i = 0; i < newValue.length; i++) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("newValue[i] = '" + newValue[i] + "'");
          }
        }
      }
    }

    if (newValue == null) {
      newValue = new String[0]; // because no selection will not submitted by browsers
    }
    uiSelectMany.setValue(newValue);
    uiSelectMany.setValid(true);
  }

// ///////////////////////////////////////////// bean getter + setter

}

