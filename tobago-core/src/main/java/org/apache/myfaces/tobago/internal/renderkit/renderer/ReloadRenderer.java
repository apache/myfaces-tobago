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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUIReload;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.AjaxUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.PartialViewContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class ReloadRenderer<T extends AbstractUIReload> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {

    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("jakarta.faces.source");
    final String clientId = component.getClientId(facesContext);
    if (clientId.equals(sourceId)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("matching = '{}'", clientId);
      }
      if (AjaxUtils.isAjaxRequest(facesContext) && component.isRendered() && !component.isUpdate()) {
        // do not render content, only render the reload tag for the next request
        final PartialViewContext partialViewContext = facesContext.getPartialViewContext();
        final String parentId = component.getParent().getClientId(facesContext);
        if (LOG.isDebugEnabled()) {
          LOG.debug("removing '{}' from lists renderIds and executeIds", clientId);
        }
        partialViewContext.getRenderIds().remove(parentId);
        partialViewContext.getExecuteIds().remove(parentId);
      }
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.TOBAGO_RELOAD);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.FREQUENCY, component.getFrequency());
  }

  @Override
  public void encodeEndInternal(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_RELOAD);
  }

}
