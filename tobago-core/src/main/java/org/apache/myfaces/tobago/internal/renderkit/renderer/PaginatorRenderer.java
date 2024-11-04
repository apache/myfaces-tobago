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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.event.SheetAction;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUIPaginator;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

public abstract class PaginatorRenderer<T extends AbstractUIPaginator> extends RendererBase<T> {

  protected void encodeLink(
      final FacesContext facesContext, final AbstractUISheet data,
      final boolean disabled, final SheetAction action, final Integer target, final Icons icon, final CssItem liClass)
      throws IOException {

    final String facet = action == SheetAction.toPage || action == SheetAction.toRow
        ? action.name() + "-" + target
        : action.name();
    final AbstractUILink command = data.ensurePagingCommand(facesContext, data, facet, facet, disabled);
    if (target != null) {
      ComponentUtils.setAttribute(command, Attributes.pagingTarget, target);
    }

    final Locale locale = facesContext.getViewRoot().getLocale();
    final String message = ResourceUtils.getString(facesContext, action.getBundleKey());
    final String tip = new MessageFormat(message, locale).format(new Integer[]{target}); // needed fot ToPage

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.LI);
    writer.writeClassAttribute(liClass, disabled ? BootstrapClass.DISABLED : null, BootstrapClass.PAGE_ITEM);
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.PAGE_LINK);
    writer.writeIdAttribute(command.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    if (icon != null) {
      writer.startElement(HtmlElements.I);
      writer.writeClassAttribute(icon);
      writer.endElement(HtmlElements.I);
    } else {
      writer.writeText(String.valueOf(target));
    }
    if (!disabled) {
      encodeBehavior(writer, facesContext, command);
    }
    data.getFacets().remove(facet);
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.LI);
  }

}
