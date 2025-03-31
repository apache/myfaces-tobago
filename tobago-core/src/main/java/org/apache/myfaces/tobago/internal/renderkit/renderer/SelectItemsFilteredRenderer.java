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

import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectItemsFiltered;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class SelectItemsFilteredRenderer<T extends AbstractUISelectItemsFiltered> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String SUFFIX_QUERY = ComponentUtils.SUB_SEPARATOR + "query";

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    final String clientIdWithSuffix = component.getClientId(facesContext) + SUFFIX_QUERY;
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(clientIdWithSuffix)) {
      final String query = requestParameterMap.get(clientIdWithSuffix);
      component.setQuery(query);
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientIdWithSuffix = component.getClientId(facesContext) + SUFFIX_QUERY;
    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(clientIdWithSuffix);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.NAME, clientIdWithSuffix, false);
    writer.writeAttribute(DataAttributes.DELAY, component.getDelay());
    writer.writeAttribute(DataAttributes.MIN_CHARS, component.getMinimumCharacters());
    writer.endElement(HtmlElements.INPUT);
  }
}
