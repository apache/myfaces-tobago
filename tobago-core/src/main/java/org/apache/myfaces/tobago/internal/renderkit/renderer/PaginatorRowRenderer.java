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

import org.apache.myfaces.tobago.internal.component.AbstractUIPaginatorRow;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

public class PaginatorRowRenderer<T extends AbstractUIPaginatorRow> extends PaginatorRenderer<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T paginator) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISheet sheet = paginator.getPageable();
    final boolean visible = paginator.isAlwaysVisible() || sheet.needMoreThanOnePage();

    writer.startElement(HtmlElements.TOBAGO_PAGINATOR_ROW);
    final String clientId = paginator.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        visible ? null : BootstrapClass.D_NONE,
        BootstrapClass.PAGINATION,
        paginator.getCustomClass());
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.PAGE_ITEM);
    writer.writeAttribute(HtmlAttributes.TITLE,
        ResourceUtils.getString(facesContext, "sheet.setRow"), true);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(
        BootstrapClass.PAGE_LINK,
        BootstrapClass.TEXT_NOWRAP);

    if (sheet.getRowCount() != 0) {
      final Locale locale = facesContext.getViewRoot().getLocale();
      final int first = sheet.getFirst() + 1;
      final int last1 = sheet.hasRowCount()
          ? sheet.getLastRowIndexOfCurrentPage()
          : -1;
      final boolean unknown = !sheet.hasRowCount();
      final String key; // plural
      if (unknown) {
        key = first == last1 ? "sheet.rowX" : "sheet.rowXtoY";
      } else {
        key = first == last1 ? "sheet.rowXofZ" : "sheet.rowXtoYofZ";
      }
      final String inputMarker = "{#}";
      final Object[] args = {inputMarker, last1 == -1 ? "?" : last1, unknown ? "" : sheet.getRowCount()};
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
        final int maxLength = Integer.toString(sheet.getRowCount()).length();
        if (!unknown) {
          writer.writeAttribute(HtmlAttributes.MAXLENGTH, maxLength);
        }
        writer.writeAttribute(HtmlAttributes.PATTERN, StringUtils.positiveNumberRegexp(maxLength), true);
        writer.endElement(HtmlElements.INPUT);
        writer.writeText(formatted.substring(pos + inputMarker.length()));
      } else {
        writer.writeText(formatted);
      }
    }
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.SPAN);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_PAGINATOR_ROW);
  }
}
