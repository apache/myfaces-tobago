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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumn;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumnBase;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.ShowPosition;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
* TBD: This is a temporary version of the SheetRenderer.
 *
 * Evaluating the best implementation... This is a try to use modern CSS for scrollable tables with fixed headers.
* */
public class SheetRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SheetRenderer.class);

  protected static final String WIDTHS = ComponentUtils.SUB_SEPARATOR + "widths";
  protected static final String SCROLL_POSITION = ComponentUtils.SUB_SEPARATOR + "scrollPosition";
  protected static final String SELECTED = ComponentUtils.SUB_SEPARATOR + "selected";
  protected static final String SELECTOR_DROPDOWN = ComponentUtils.SUB_SEPARATOR + "selectorDropdown";

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    final UISheet sheet = (UISheet) component;
    final String clientId = sheet.getClientId(facesContext);

    final String value = facesContext.getExternalContext().getRequestParameterMap().get(clientId + SCROLL_POSITION);
    if (value != null) {
      sheet.getState().getScrollPosition().update(value);
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    final UISheet sheet = (UISheet) component;
    final String clientId = sheet.getClientId(facesContext);
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final List<AbstractUIColumnBase> columns = sheet.getRenderedColumns();
    final Selectable selectable = sheet.getSelectable();
    final List<Integer> selectedRows = getSelectedRows(sheet);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, sheet);
    writer.writeClassAttribute(Classes.create(sheet), sheet.getCustomClass());
    writer.writeStyleAttribute(sheet.getStyle());
    final UIComponent facetReload = ComponentUtils.getFacet(sheet, Facets.reload);
    if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
      final UIReload update = (UIReload) facetReload;
      writer.writeAttribute(DataAttributes.RELOAD, update.getFrequency());
    }
    final String[] clientIds = ComponentUtils.evaluateClientIds(facesContext, sheet, sheet.getRenderedPartially());
    if (clientIds.length > 0) {
      writer.writeAttribute(DataAttributes.PARTIAL_IDS, JsonUtils.encode(clientIds), true);
    }
    writer.writeAttribute(DataAttributes.SELECTION_MODE, sheet.getSelectable().name(), false);
    writer.writeAttribute(DataAttributes.FIRST, Integer.toString(sheet.getFirst()), false);

//    writer.startElement(HtmlElements.INPUT);
//    writer.writeIdAttribute(clientId + WIDTHS);
//    writer.writeNameAttribute(clientId + WIDTHS);
//    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
//    writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.joinWithSurroundingSeparator(columnWidths), false);
//    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(clientId + SCROLL_POSITION);
    writer.writeNameAttribute(clientId + SCROLL_POSITION);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.VALUE, sheet.getState().getScrollPosition().encode(), false);
    writer.writeAttribute(DataAttributes.SCROLL_POSITION, Boolean.TRUE.toString(), true);
    writer.endElement(HtmlElements.INPUT);

    if (selectable != Selectable.none) {
      writer.startElement(HtmlElements.INPUT);
      writer.writeIdAttribute(clientId + SELECTED);
      writer.writeNameAttribute(clientId + SELECTED);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
      writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.joinWithSurroundingSeparator(selectedRows), true);
      writer.endElement(HtmlElements.INPUT);
    }

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(Classes.create(sheet, "body"));

    writer.startElement(HtmlElements.TABLE);
    writer.writeClassAttribute(Classes.create(sheet, "fixHeader"), BootstrapClass.TABLE);

    writer.startElement(HtmlElements.THEAD);
    writer.startElement(HtmlElements.TR);
    for (AbstractUIColumnBase column : columns) {
      writer.startElement(HtmlElements.TH);
      writer.writeIdAttribute(column.getClientId(facesContext));
      if (column instanceof UIColumn) {
        writer.writeText(((UIColumn) column).getLabel());
      } else if (column instanceof UIColumnSelector && selectable.isMulti()) {
        writer.writeClassAttribute(Classes.create(sheet, "selectorDropdown"));

        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(BootstrapClass.DROPDOWN);
        writer.startElement(HtmlElements.BUTTON);
        writer.writeClassAttribute(BootstrapClass.BTN, BootstrapClass.BTN_SECONDARY, BootstrapClass.DROPDOWN_TOGGLE);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
        writer.writeIdAttribute(clientId + SELECTOR_DROPDOWN);
        writer.writeAttribute(DataAttributes.TOGGLE, "dropdown", false);
        writer.writeAttribute(Arias.HASPOPUP, Boolean.TRUE.toString(), false);
        writer.writeAttribute(Arias.EXPANDED, Boolean.FALSE.toString(), false);
        writer.endElement(HtmlElements.BUTTON);
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(BootstrapClass.DROPDOWN_MENU);
        writer.writeAttribute(Arias.LABELLEDBY, clientId + SELECTOR_DROPDOWN, false);
        writer.startElement(HtmlElements.BUTTON);
        writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
        writer.writeAttribute(DataAttributes.COMMAND, "sheetSelectAll", false);
        writer.writeText(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetMenuSelect"));
        writer.endElement(HtmlElements.BUTTON);
        writer.startElement(HtmlElements.BUTTON);
        writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
        writer.writeAttribute(DataAttributes.COMMAND, "sheetDeselectAll", false);
        writer.writeText(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetMenuUnselect"));
        writer.endElement(HtmlElements.BUTTON);
        writer.startElement(HtmlElements.BUTTON);
        writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
        writer.writeAttribute(DataAttributes.COMMAND, "sheetToggleAll", false);
        writer.writeText(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetMenuToggleselect"));
        writer.endElement(HtmlElements.BUTTON);
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.DIV);
      } else {

      }
      writer.endElement(HtmlElements.TH);
    }
    writer.endElement(HtmlElements.TR);
    writer.endElement(HtmlElements.THEAD);

  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
    final UISheet sheet = (UISheet) component;
    final String clientId = sheet.getClientId(facesContext);
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final List<AbstractUIColumnBase> columns = sheet.getRenderedColumns();
    final boolean hasClickAction = HtmlRendererUtils.renderSheetCommands(sheet, facesContext, writer);
    final List<Integer> selectedRows = getSelectedRows(sheet);

    writer.startElement(HtmlElements.TBODY);
    final String var = sheet.getVar();

    boolean emptySheet = true;
    // rows = 0 means: show all
    final int last = sheet.isRowsUnlimited() ? Integer.MAX_VALUE : sheet.getFirst() + sheet.getRows();
    for (int rowIndex = sheet.getFirst(); rowIndex < last; rowIndex++) {
      sheet.setRowIndex(rowIndex);
      if (!sheet.isRowAvailable()) {
        break;
      }

      final Object rowRendered = sheet.getAttributes().get("rowRendered");
      if (rowRendered instanceof Boolean && !((Boolean) rowRendered)) {
        continue;
      }

      emptySheet = false;

      if (LOG.isDebugEnabled()) {
        LOG.debug("var       " + var);
        LOG.debug("list      " + sheet.getValue());
      }

      writer.startElement(HtmlElements.TR);

      final boolean selected = selectedRows.contains(rowIndex);
      final String[] rowMarkups = (String[]) sheet.getAttributes().get("rowMarkup");
      Markup rowMarkup = Markup.NULL;
      if (selected) {
        rowMarkup = rowMarkup.add(Markup.SELECTED);
      }
      if (rowMarkups != null) {
        rowMarkup = rowMarkup.add(Markup.valueOf(rowMarkups));
      }
      writer.writeClassAttribute(Classes.create(sheet, "row", rowMarkup), selected ? BootstrapClass.INFO : null);

      for (AbstractUIColumnBase column : columns) {

        writer.startElement(HtmlElements.TD);

        Markup markup = Markup.NULL;
        if (column instanceof AbstractUIColumn) {
          final Markup markup1 = ((AbstractUIColumn) column).getMarkup();
          if (markup1 != null) {
            markup = markup1;
          }
          if (hasClickAction) {
            markup = markup.add(Markup.CLICKABLE);
          }
          final String textAlign = ComponentUtils.getStringAttribute(column, Attributes.align);
          if (textAlign != null) {
            switch (TextAlign.valueOf(textAlign)) {
              case right:
                markup = markup.add(Markup.RIGHT);
                break;
              case center:
                markup = markup.add(Markup.CENTER);
                break;
              case justify:
                markup = markup.add(Markup.JUSTIFY);
                break;
              default:
                // nothing to do
            }
          }
        }
        writer.writeClassAttribute(Classes.create(sheet, "cell", markup));
LOG.info("" + column);
        if (column instanceof UIColumnSelector) {
          UIColumnSelector selector = (UIColumnSelector) column;
          writer.startElement(HtmlElements.INPUT);
          if (sheet.getSelectable().isSingle()) {
            writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO);
          } else {
            writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
          }
          writer.writeAttribute(HtmlAttributes.CHECKED, selected);
          writer.writeAttribute(HtmlAttributes.DISABLED, selector.isDisabled());
          writer.writeClassAttribute(Classes.create(sheet, "columnSelector"));
          writer.endElement(HtmlElements.INPUT);
        } else if (column instanceof UIColumn) {
          for (final UIComponent grandKid : column.getChildren()) {
            if (grandKid.isRendered()) {
              RenderUtils.encode(facesContext, grandKid);
            }
          }
        } else {

        }
        writer.endElement(HtmlElements.TD);
      }

      writer.endElement(HtmlElements.TR);

    }
    writer.endElement(HtmlElements.TBODY);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final UISheet sheet = (UISheet) component;
    final String clientId = sheet.getClientId(facesContext);
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final Application application = facesContext.getApplication();

    writer.endElement(HtmlElements.TABLE);
    writer.endElement(HtmlElements.DIV);

    if (sheet.isPagingVisible()) {
      writer.startElement(HtmlElements.FOOTER);
      writer.writeClassAttribute(Classes.create(sheet, "footer"));

      // show row range
      final Markup showRowRange = markupForLeftCenterRight(sheet.getShowRowRange());
      if (showRowRange != Markup.NULL) {
        final UICommand command
            = ensurePagingCommand(application, sheet, Facets.pagerRow.name(), PageAction.TO_ROW, false);
        final String pagerCommandId = command.getClientId(facesContext);

        writer.startElement(HtmlElements.UL);
        writer.writeClassAttribute(Classes.create(sheet, "paging", showRowRange), BootstrapClass.PAGINATION);
        writer.startElement(HtmlElements.LI);
        writer.writeClassAttribute(BootstrapClass.PAGE_ITEM);
        writer.writeAttribute(HtmlAttributes.TITLE,
            ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetPagingInfoRowPagingTip"), true);
        writer.startElement(HtmlElements.SPAN);
        writer.writeClassAttribute(Classes.create(sheet, "pagingText"), BootstrapClass.PAGE_LINK);
        if (sheet.getRowCount() != 0) {
          final Locale locale = facesContext.getViewRoot().getLocale();
          final int first = sheet.getFirst() + 1;
          final int last1 = sheet.hasRowCount()
              ? sheet.getLastRowIndexOfCurrentPage()
              : -1;
          final boolean unknown = !sheet.hasRowCount();
          final String key; // plural
          if (unknown) {
            if (first == last1) {
              key = "sheetPagingInfoUndefinedSingleRow";
            } else {
              key = "sheetPagingInfoUndefinedRows";
            }
          } else {
            if (first == last1) {
              key = "sheetPagingInfoSingleRow";
            } else {
              key = "sheetPagingInfoRows";
            }
          }
          final String inputMarker = "{#}";
          final Object[] args = {inputMarker, last1 == -1 ? "?" : last1, unknown ? "" : sheet.getRowCount()};
          final MessageFormat detail = new MessageFormat(
              ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", key), locale);
          final String formatted = detail.format(args);
          final int pos = formatted.indexOf(inputMarker);
          if (pos >= 0) {
            writer.writeText(formatted.substring(0, pos));
            writer.startElement(HtmlElements.SPAN);
            writer.writeClassAttribute(TobagoClass.SHEET__PAGING_OUTPUT);
            writer.writeText(Integer.toString(first));
            writer.endElement(HtmlElements.SPAN);
            writer.startElement(HtmlElements.INPUT);
            writer.writeIdAttribute(pagerCommandId + ComponentUtils.SUB_SEPARATOR + "value");
            writer.writeNameAttribute(pagerCommandId + ComponentUtils.SUB_SEPARATOR + "value");
            writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
            writer.writeClassAttribute(TobagoClass.SHEET__PAGING_INPUT);
            writer.writeAttribute(HtmlAttributes.VALUE, first);
            if (!unknown) {
              writer.writeAttribute(HtmlAttributes.MAXLENGTH, Integer.toString(sheet.getRowCount()).length());
            }
            writer.endElement(HtmlElements.INPUT);
            writer.writeText(formatted.substring(pos + inputMarker.length()));
          } else {
            writer.writeText(formatted);
          }
        } else {
          writer.write(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetPagingInfoEmptyRow"));
        }
        writer.endElement(HtmlElements.SPAN);
        writer.endElement(HtmlElements.LI);
        writer.endElement(HtmlElements.UL);
      }

      // show direct links
      final Markup showDirectLinks = markupForLeftCenterRight(sheet.getShowDirectLinks());
      if (showDirectLinks != Markup.NULL) {
        writer.startElement(HtmlElements.UL);
        writer.writeClassAttribute(
            Classes.create(sheet, "paging", showDirectLinks), BootstrapClass.PAGINATION);
        if (sheet.isShowDirectLinksArrows()) {
          final boolean disabled = sheet.isAtBeginning();
          encodeLink(facesContext, sheet, application, disabled, PageAction.FIRST, null, Icons.STEP_BACKWARD, null);
          encodeLink(facesContext, sheet, application, disabled, PageAction.PREV, null, Icons.BACKWARD, null);
        }
        encodeDirectPagingLinks(facesContext, application, sheet);
        if (sheet.isShowDirectLinksArrows()) {
          final boolean disabled = sheet.isAtEnd();
          encodeLink(facesContext, sheet, application, disabled, PageAction.NEXT, null, Icons.FORWARD, null);
          encodeLink(facesContext, sheet, application, disabled || !sheet.hasRowCount(), PageAction.LAST, null,
              Icons.STEP_FORWARD, null);
        }
        writer.endElement(HtmlElements.UL);
      }

      // show page range
      final Markup showPageRange = markupForLeftCenterRight(sheet.getShowPageRange());
      if (showPageRange != Markup.NULL) {
        final UICommand command
            = ensurePagingCommand(application, sheet, Facets.pagerPage.name(), PageAction.TO_PAGE, false);
        final String pagerCommandId = command.getClientId(facesContext);

        writer.startElement(HtmlElements.UL);
        writer.writeClassAttribute(Classes.create(sheet, "paging", showPageRange), BootstrapClass.PAGINATION);

        if (sheet.isShowPageRangeArrows()) {
          final boolean disabled = sheet.isAtBeginning();
          encodeLink(facesContext, sheet, application, disabled, PageAction.FIRST, null, Icons.STEP_BACKWARD, null);
          encodeLink(facesContext, sheet, application, disabled, PageAction.PREV, null, Icons.BACKWARD, null);
        }
        writer.startElement(HtmlElements.LI);
        writer.writeClassAttribute(BootstrapClass.PAGE_ITEM);
        writer.startElement(HtmlElements.SPAN);
        writer.writeClassAttribute(Classes.create(sheet, "pagingText"), BootstrapClass.PAGE_LINK);
        writer.writeAttribute(HtmlAttributes.TITLE,
            ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetPagingInfoPagePagingTip"), true);
        if (sheet.getRowCount() != 0) {
          final Locale locale = facesContext.getViewRoot().getLocale();
          final int first = sheet.getCurrentPage() + 1;
          final boolean unknown = !sheet.hasRowCount();
          final int pages = unknown ? -1 : sheet.getPages();
          final String key;
          if (unknown) {
            if (first == pages) {
              key = "sheetPagingInfoUndefinedSinglePage";
            } else {
              key = "sheetPagingInfoUndefinedPages";
            }
          } else {
            if (first == pages) {
              key = "sheetPagingInfoSinglePage";
            } else {
              key = "sheetPagingInfoPages";
            }
          }
          final String inputMarker = "{#}";
          final Object[] args = {inputMarker, pages == -1 ? "?" : pages};
          final MessageFormat detail = new MessageFormat(
              ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", key), locale);
          final String formatted = detail.format(args);
          final int pos = formatted.indexOf(inputMarker);
          if (pos >= 0) {
            writer.writeText(formatted.substring(0, pos));
            writer.startElement(HtmlElements.SPAN);
            writer.writeClassAttribute(TobagoClass.SHEET__PAGING_OUTPUT);
            writer.writeText(Integer.toString(first));
            writer.endElement(HtmlElements.SPAN);
            writer.startElement(HtmlElements.INPUT);
            writer.writeIdAttribute(pagerCommandId + ComponentUtils.SUB_SEPARATOR + "value");
            writer.writeNameAttribute(pagerCommandId + ComponentUtils.SUB_SEPARATOR + "value");
            writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
            writer.writeClassAttribute(TobagoClass.SHEET__PAGING_INPUT);
            writer.writeAttribute(HtmlAttributes.VALUE, first);
            if (!unknown) {
              writer.writeAttribute(HtmlAttributes.MAXLENGTH, Integer.toString(pages).length());
            }
            writer.endElement(HtmlElements.INPUT);
            writer.writeText(formatted.substring(pos + inputMarker.length()));
          } else {
            writer.writeText(formatted);
          }
        } else {
          writer.writeText(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetPagingInfoEmptyPage"));
        }
        writer.endElement(HtmlElements.SPAN);
        writer.endElement(HtmlElements.LI);
        if (sheet.isShowPageRangeArrows()) {
          final boolean disabled = sheet.isAtEnd();
          encodeLink(facesContext, sheet, application, disabled, PageAction.NEXT, null, Icons.FORWARD, null);
          encodeLink(facesContext, sheet, application, disabled || !sheet.hasRowCount(), PageAction.LAST, null,
              Icons.STEP_FORWARD, null);
        }
        writer.endElement(HtmlElements.UL);
      }

      writer.endElement(HtmlElements.FOOTER);
    }

    writer.endElement(HtmlElements.DIV);
  }

  private List<Integer> getSelectedRows(final UISheet sheet) {
    final SheetState state = sheet.getState();
    List<Integer> selected = (List<Integer>) ComponentUtils.getAttribute(sheet, Attributes.selectedListString);
    if (selected == null && state != null) {
      selected = state.getSelectedRows();
    }
    if (selected == null) {
      selected = Collections.emptyList();
    }
    return selected;
  }

  private void encodeDirectPagingLinks(
      final FacesContext facesContext, final Application application, final UISheet sheet)
      throws IOException {

    final UICommand command
        = ensurePagingCommand(application, sheet, Facets.PAGER_PAGE_DIRECT, PageAction.TO_PAGE, false);
    int linkCount = ComponentUtils.getIntAttribute(sheet, Attributes.directLinkCount);
    linkCount--;  // current page needs no link
    final ArrayList<Integer> prevs = new ArrayList<Integer>(linkCount);
    int page = sheet.getCurrentPage() + 1;
    for (int i = 0; i < linkCount && page > 1; i++) {
      page--;
      if (page > 0) {
        prevs.add(0, page);
      }
    }

    final ArrayList<Integer> nexts = new ArrayList<Integer>(linkCount);
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

    int skip = prevs.size() > 0 ? prevs.get(0) : 1;
    if (!sheet.isShowDirectLinksArrows() && skip > 1) {
      skip -= (linkCount - (linkCount / 2));
      skip--;
      if (skip < 1) {
        skip = 1;
      }
      encodeLink(facesContext, sheet, application, false, PageAction.TO_PAGE, skip, Icons.ELLIPSIS_H, null);
    }
    for (final Integer prev : prevs) {
      encodeLink(facesContext, sheet, application, false, PageAction.TO_PAGE, prev, null, null);
    }
    encodeLink(facesContext, sheet, application, false, PageAction.TO_PAGE,
        sheet.getCurrentPage() + 1, null, BootstrapClass.ACTIVE);

    for (final Integer next : nexts) {
      encodeLink(facesContext, sheet, application, false, PageAction.TO_PAGE, next, null, null);
    }

    skip = nexts.size() > 0 ? nexts.get(nexts.size() - 1) : pages;
    if (!sheet.isShowDirectLinksArrows() && skip < pages) {
      skip += linkCount / 2;
      skip++;
      if (skip > pages) {
        skip = pages;
      }
      encodeLink(facesContext, sheet, application, false, PageAction.TO_PAGE, skip, Icons.ELLIPSIS_H, null);
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  private void encodeLink(
      final FacesContext facesContext, final UISheet data, final Application application,
      final boolean disabled, final PageAction action, Integer target, Icons icon, CssItem liClass)
      throws IOException {

    final String facet = action == PageAction.TO_PAGE || action == PageAction.TO_ROW
        ? action.getToken() + "-" + target
        : action.getToken();
    final UICommand command = ensurePagingCommand(application, data, facet, action, disabled);
    if (target != null) {
      ComponentUtils.setAttribute(command, Attributes.pagingTarget, target);
    }
    command.setRenderedPartially(new String[]{data.getId()});

    final Locale locale = facesContext.getViewRoot().getLocale();
    final String message = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheet" + action.getToken());
    final String tip = new MessageFormat(message, locale).format(new Integer[]{target}); // needed fot ToPage

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.LI);
    writer.writeClassAttribute(liClass, disabled ? BootstrapClass.DISABLED : null, BootstrapClass.PAGE_ITEM);
    writer.startElement(HtmlElements.A);
    writer.writeClassAttribute(BootstrapClass.PAGE_LINK);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeIdAttribute(command.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    if (!disabled) {
      final CommandMap map = new CommandMap(new Command(facesContext, command));
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);
    }
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    if (icon != null) {
      writer.writeIcon(icon);
    } else {
      writer.writeText(String.valueOf(target));
    }
    writer.endElement(HtmlElements.A);
    writer.endElement(HtmlElements.LI);
  }

  private UICommand ensurePagingCommand(
      final Application application, final UISheet sheet, final String facet, final PageAction action,
      final boolean disabled) {

    final Map<String, UIComponent> facets = sheet.getFacets();
    UICommand command = (UICommand) facets.get(facet);
    if (command == null) {
      command = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
      command.setRendererType(RendererTypes.SHEET_PAGE_COMMAND);
      command.setRendered(true);
      ComponentUtils.setAttribute(command, Attributes.pageAction, action);
      command.setDisabled(disabled);
      facets.put(facet, command);
    }
    return command;
  }

  private Markup markupForLeftCenterRight(final ShowPosition position) {
    switch (position) {
      case left:
        return Markup.LEFT;
      case center:
        return Markup.CENTER;
      case right:
        return Markup.RIGHT;
      default:
        return Markup.NULL;
    }
  }

}
