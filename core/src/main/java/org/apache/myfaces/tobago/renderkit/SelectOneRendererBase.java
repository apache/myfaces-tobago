package org.apache.myfaces.tobago.renderkit;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class SelectOneRendererBase extends InputRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectOneRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UISelectOne uiSelectOne = (UISelectOne) component;

    String clientId = uiSelectOne.getClientId(facesContext);
    Object newValue =
        facesContext.getExternalContext().getRequestParameterMap().get(clientId);
    if (LOG.isDebugEnabled()) {
      LOG.debug("decode: key='" + clientId + "' value='" + newValue + "'");
    }
    uiSelectOne.setSubmittedValue(newValue);
  }


  protected abstract void renderMain(FacesContext facesContext, UIComponent input,
      TobagoResponseWriter writer) throws IOException;

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    super.encodeEndTobago(facesContext, component);
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();


    renderMain(facesContext, component, writer);
  }

}

