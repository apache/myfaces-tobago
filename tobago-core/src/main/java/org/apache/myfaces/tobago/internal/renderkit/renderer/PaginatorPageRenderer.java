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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.event.SheetAction;
import org.apache.myfaces.tobago.internal.component.AbstractUIPaginatorPage;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

public class PaginatorPageRenderer<T extends AbstractUIPaginatorPage> extends PaginatorRenderer<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T paginator) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISheet sheet = paginator.getPageable();
    final boolean visible = paginator.isAlwaysVisible() || sheet.needMoreThanOnePage();

    writer.startElement(HtmlElements.TOBAGO_PAGINATOR_PAGE);
    final String clientId = paginator.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        visible ? null : BootstrapClass.D_NONE,
        paginator.getCustomClass());

    writer.startElement(HtmlElements.UL);
    writer.writeClassAttribute(BootstrapClass.PAGINATION);

    if (sheet.isShowPageRangeArrows()) {
      final boolean disabled = sheet.isAtBeginning();
      encodeLink(
          facesContext, disabled, SheetAction.first, null, Icons.SKIP_START, null);
      encodeLink(facesContext, disabled, SheetAction.prev, null, Icons.CARET_LEFT, null);
    }
    writer.startElement(HtmlElements.LI);
    writer.writeClassAttribute(BootstrapClass.PAGE_ITEM);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(
        TobagoClass.PAGING,
        BootstrapClass.PAGE_LINK,
        BootstrapClass.TEXT_NOWRAP);
    writer.writeAttribute(HtmlAttributes.TITLE,
        ResourceUtils.getString(facesContext, "sheet.setPage"), true);
    if (sheet.getRowCount() != 0) {
      final Locale locale = facesContext.getViewRoot().getLocale();
      final int first = sheet.getCurrentPage() + 1;
      final boolean unknown = !sheet.hasRowCount();
      final int pages = unknown ? -1 : sheet.getPages();
      final String key;
      if (unknown) {
        key = first == pages ? "sheet.pageX" : "sheet.pageXtoY";
      } else {
        key = first == pages ? "sheet.pageXofZ" : "sheet.pageXtoYofZ";
      }
      final String inputMarker = "{#}";
      final Object[] args = {inputMarker, pages == -1 ? "?" : pages};
      final MessageFormat detail = new MessageFormat(ResourceUtils.getString(facesContext, key), locale);
      final String formatted = detail.format(args);
      final int pos = formatted.indexOf(inputMarker);
      if (pos >= 0) {
        writer.writeText(formatted.substring(0, pos));
        writer.startElement(HtmlElements.SPAN);
        writer.writeText(Integer.toString(first));
        writer.endElement(HtmlElements.SPAN);
        writer.startElement(HtmlElements.INPUT);
        writer.writeIdAttribute(paginator.getFieldId(facesContext));
        writer.writeNameAttribute(clientId);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
        writer.writeAttribute(HtmlAttributes.VALUE, first);
        if (!unknown) {
          writer.writeAttribute(HtmlAttributes.MAXLENGTH, Integer.toString(pages).length());
        }
        writer.endElement(HtmlElements.INPUT);
        writer.writeText(formatted.substring(pos + inputMarker.length()));
      } else {
        writer.writeText(formatted);
      }
    }
    ComponentUtils.removeFacet(sheet, Facets.pagerPage);
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.LI);
    if (sheet.isShowPageRangeArrows()) {
      final boolean disabled = sheet.isAtEnd();
      encodeLink(facesContext, disabled, SheetAction.next, null, Icons.CARET_RIGHT, null);
      encodeLink(facesContext, disabled || !sheet.hasRowCount(), SheetAction.last, null, Icons.SKIP_END, null);
    }

    writer.endElement(HtmlElements.UL);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_PAGINATOR_PAGE);
  }
}
