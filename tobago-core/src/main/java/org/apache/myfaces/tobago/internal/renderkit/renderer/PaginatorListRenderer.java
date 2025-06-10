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

import org.apache.myfaces.tobago.event.SheetAction;
import org.apache.myfaces.tobago.internal.component.AbstractUIPaginatorList;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.layout.Arrows;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class PaginatorListRenderer<T extends AbstractUIPaginatorList> extends PaginatorRenderer<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T paginator) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = paginator.getClientId(facesContext);
    final AbstractUISheet sheet = paginator.getPageable();
    final boolean visible = paginator.isAlwaysVisible() || sheet.needMoreThanOnePage();

    writer.startElement(HtmlElements.TOBAGO_PAGINATOR_LIST);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        visible ? null : BootstrapClass.D_NONE,
        paginator.getCustomClass());

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeIdAttribute(paginator.getFieldId(facesContext));
    writer.writeNameAttribute(clientId);
    writer.endElement(HtmlElements.INPUT);

    if (sheet != null) {
      writer.startElement(HtmlElements.UL);
      writer.writeClassAttribute(BootstrapClass.PAGINATION);

      int linkCount = paginator.getMax();
      linkCount--;  // the current page needs no link
      final ArrayList<Integer> prevs = new ArrayList<>(linkCount);
      int page = sheet.getCurrentPage() + 1;
      for (int i = 0; i < linkCount && page > 1; i++) {
        page--;
        if (page > 0) {
          prevs.add(0, page);
        }
      }

      final ArrayList<Integer> nexts = new ArrayList<>(linkCount);
      page = sheet.getCurrentPage() + 1;
      final int pages = sheet.hasRowCount() || sheet.isRowsUnlimited() ? sheet.getPages() : Integer.MAX_VALUE;
      for (int i = 0; i < linkCount && page < pages; i++) {
        page++;
        if (page > 1) {
          nexts.add(page);
        }
      }

      if (prevs.size() > (linkCount / 2)
          && nexts.size() > (linkCount - (linkCount / 2))) {
        while (prevs.size() > (linkCount / 2)) {
          prevs.remove(0);
        }
        while (nexts.size() > (linkCount - (linkCount / 2))) {
          nexts.remove(nexts.size() - 1);
        }
      } else if (prevs.size() <= (linkCount / 2)) {
        while (prevs.size() + nexts.size() > linkCount) {
          nexts.remove(nexts.size() - 1);
        }
      } else {
        while (prevs.size() + nexts.size() > linkCount) {
          prevs.remove(0);
        }
      }

      final Arrows arrows = paginator.getArrows();
      final boolean atBeginning = sheet.isAtBeginning();
      if (arrows == Arrows.show || arrows == Arrows.auto && !atBeginning) {
        encodeLink(facesContext, atBeginning, SheetAction.first, null, Icons.SKIP_START, null);
        encodeLink(facesContext, atBeginning, SheetAction.prev, null, Icons.CARET_LEFT, null);
      }

      int skip = !prevs.isEmpty() ? prevs.get(0) : 1;
      if (!(arrows == Arrows.show || arrows == Arrows.auto) && skip > 1) {
        skip -= linkCount - (linkCount / 2);
        skip--;
        if (skip < 1) {
          skip = 1;
        }
        encodeLink(facesContext, false, SheetAction.toPage, skip, Icons.THREE_DOTS, null);
      }

      for (final Integer prev : prevs) {
        encodeLink(facesContext, false, SheetAction.toPage, prev, null, null);
      }

      encodeLink(facesContext, false, SheetAction.toPage, sheet.getCurrentPage() + 1, null, BootstrapClass.ACTIVE);

      for (final Integer next : nexts) {
        encodeLink(facesContext, false, SheetAction.toPage, next, null, null);
      }

      skip = !nexts.isEmpty() ? nexts.get(nexts.size() - 1) : pages;
      if (!(arrows == Arrows.show || arrows == Arrows.auto) && skip < pages) {
        skip += linkCount / 2;
        skip++;
        if (skip > pages) {
          skip = pages;
        }
        encodeLink(facesContext, false, SheetAction.toPage, skip, Icons.THREE_DOTS, null);
      }

      final boolean atEnd = sheet.isAtEnd();
      if (arrows == Arrows.show || arrows == Arrows.auto && !atEnd) {
        encodeLink(facesContext, atEnd, SheetAction.next, null, Icons.CARET_RIGHT, null);
        encodeLink(facesContext, atEnd || !sheet.hasRowCount(), SheetAction.last, null, Icons.SKIP_END, null);
      }

      writer.endElement(HtmlElements.UL);
    } else {
      LOG.warn("No sheet found for paginator with clientId='{}'!", clientId);
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_PAGINATOR_LIST);
  }
}
