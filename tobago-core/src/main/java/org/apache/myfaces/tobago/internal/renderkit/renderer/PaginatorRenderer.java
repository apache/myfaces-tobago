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

import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SheetAction;
import org.apache.myfaces.tobago.internal.component.AbstractUIPaginator;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

public abstract class PaginatorRenderer<T extends AbstractUIPaginator> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T paginator) {
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String sourceId = requestParameterMap.get(ClientBehaviorContext.BEHAVIOR_SOURCE_PARAM_NAME);
    final String clientId = paginator.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("clientId           {}", clientId);
      LOG.debug("javax.faces.source {}", sourceId);
    }
    if (clientId.equals(sourceId)) {
      final String value = requestParameterMap.get(clientId);
      if (value != null) {
        final AbstractUISheet sheet = paginator.getPageable();
        if (sheet != null) {
          final JsonUtils.SheetActionRecord sheetActionRecord = JsonUtils.decodeSheetAction(value);
          LOG.debug("record='{}'", sheetActionRecord);
          sheet.queueEvent(new PageActionEvent(sheet, sheetActionRecord));
        } else {
          LOG.warn("No sheet found for paginator {}", clientId);
        }
      }
    }
  }

  protected void encodeLink(
      final FacesContext facesContext,
      final boolean disabled, final SheetAction action, final Integer target, final Icons icon, final CssItem liClass)
      throws IOException {

    final Locale locale = facesContext.getViewRoot().getLocale();
    final String message = ResourceUtils.getString(facesContext, action.getBundleKey());
    final String tip = new MessageFormat(message, locale).format(new Integer[]{target}); // needed fot ToPage

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.LI);
    writer.writeClassAttribute(liClass, disabled ? BootstrapClass.DISABLED : null, BootstrapClass.PAGE_ITEM);
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.PAGE_LINK);
    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(DataAttributes.ACTION, JsonUtils.encode(action, target), false);
    if (icon != null) {
      writer.startElement(HtmlElements.I);
      writer.writeClassAttribute(icon);
      writer.endElement(HtmlElements.I);
    } else {
      writer.writeText(String.valueOf(target));
    }
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.LI);
  }

}
