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

import org.apache.myfaces.tobago.internal.component.AbstractUIFilter;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyList;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneList;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class FilterRenderer<T extends AbstractUIFilter> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    final String clientId = component.getClientId(facesContext);
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(clientId)) {
      final String query = requestParameterMap.get(clientId);
      component.setQuery(query);
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final AbstractUISelectOneList selectOneList = ComponentUtils.findAncestor(component, AbstractUISelectOneList.class);
    final AbstractUISelectManyList selectManyList =
        ComponentUtils.findAncestor(component, AbstractUISelectManyList.class);
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.TOBAGO_FILTER);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    if (selectOneList != null) {
      writer.writeAttribute(HtmlAttributes.FOR, selectOneList.getFieldId(facesContext), false);
    } else if (selectManyList != null) {
      writer.writeAttribute(HtmlAttributes.FOR, selectManyList.getFieldId(facesContext), false);
    } else {
      LOG.error("No ancestor with type AbstractUISelectManyList found for filter id={}", clientId);
    }

    writer.writeAttribute(CustomAttributes.DELAY, component.getDelay());
    writer.writeAttribute(CustomAttributes.MIN_CHARS, component.getMinimumCharacters());

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.NAME, clientId, false);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.TOBAGO_FILTER);
  }
}
