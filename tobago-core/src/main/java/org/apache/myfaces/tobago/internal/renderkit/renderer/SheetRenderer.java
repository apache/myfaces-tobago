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

import jakarta.faces.component.NamingContainer;
import jakarta.faces.component.UIColumn;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIData;
import jakarta.faces.component.behavior.AjaxBehavior;
import jakarta.faces.component.behavior.ClientBehaviorContext;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIPaginatorList;
import org.apache.myfaces.tobago.component.UIPaginatorPage;
import org.apache.myfaces.tobago.component.UIPaginatorRow;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumn;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumnBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumnNode;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumnPanel;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumnSelector;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUIOut;
import org.apache.myfaces.tobago.internal.component.AbstractUIPaginator;
import org.apache.myfaces.tobago.internal.component.AbstractUIReload;
import org.apache.myfaces.tobago.internal.component.AbstractUIRow;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.context.Nonce;
import org.apache.myfaces.tobago.internal.layout.Cell;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.StyleRenderUtils;
import org.apache.myfaces.tobago.layout.Arrows;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.layout.PaginatorMode;
import org.apache.myfaces.tobago.layout.ShowPosition;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.model.SortedColumn;
import org.apache.myfaces.tobago.model.SortedColumnList;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SheetRenderer<T extends AbstractUISheet> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String SUFFIX_WIDTHS = ComponentUtils.SUB_SEPARATOR + "widths";
  private static final String SUFFIX_COLUMN_RENDERED = ComponentUtils.SUB_SEPARATOR + "rendered";
  private static final String SUFFIX_SCROLL_POSITION = ComponentUtils.SUB_SEPARATOR + "scrollPosition";
  private static final String SUFFIX_SELECTED = ComponentUtils.SUB_SEPARATOR + "selected";
  private static final String SUFFIX_LAZY = NamingContainer.SEPARATOR_CHAR + "pageActionlazy";
  private static final String SUFFIX_LAZY_SCROLL_POSITION = ComponentUtils.SUB_SEPARATOR + "lazyScrollPosition";
  private static final String SUFFIX_COLUMN_SELECTOR = ComponentUtils.SUB_SEPARATOR + "columnSelector";

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {

    final List<AbstractUIColumnBase> columns = component.getAllColumns();
    final String clientId = component.getClientId(facesContext);

    String key = clientId + SUFFIX_WIDTHS;
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final SheetState state = component.getState();
    if (requestParameterMap.containsKey(key)) {
      final String widths = requestParameterMap.get(key);
      ensureColumnWidthsSize(state.getColumnWidths(), columns, JsonUtils.decodeIntegerArray(widths));
    }

    key = clientId + SUFFIX_SELECTED;
    if (requestParameterMap.containsKey(key)) {
      final String selected = requestParameterMap.get(key);
      if (LOG.isDebugEnabled()) {
        LOG.debug("selected = " + selected);
      }
      List<Integer> selectedRows;
      try {
        selectedRows = JsonUtils.decodeIntegerArray(selected);
      } catch (final NumberFormatException e) {
        LOG.warn(selected, e);
        selectedRows = Collections.emptyList();
      }

      ComponentUtils.setAttribute(component, Attributes.selectedListString, selectedRows);
    }

    if (component.isLazy()) {
      if (component.getRows() > 0) {
        component.setRows(0);
        LOG.warn("The 'rows' attribute must not be used for lazy sheets. Use 'lazyRows' instead. SheetId={}",
            component.getClientId(facesContext));
      }

      final String lazyScrollPosition = requestParameterMap.get(clientId + SUFFIX_LAZY_SCROLL_POSITION);
      if (lazyScrollPosition != null) {
        state.getLazyScrollPosition().update(lazyScrollPosition);
      }
      if (component.isLazyUpdate(facesContext)) {
        component.setLazyUpdate(true);
        key = "tobago.sheet.lazyFirstRow";
        if (requestParameterMap.containsKey(key)) {
          component.setLazyFirstRow(Integer.parseInt(requestParameterMap.get(key)));
        }
        facesContext.renderResponse();
      }
    }

    final String value = requestParameterMap.get(clientId + SUFFIX_SCROLL_POSITION);
    if (value != null) {
      state.getScrollPosition().update(value);
    }
    RenderUtils.decodedStateOfTreeData(facesContext, component);

    decodeColumnAction(facesContext, columns);
/* this will be done by the jakarta.faces.component.UIData.processDecodes() because these are facets.
    for (UIComponent facet : sheet.getFacets().values()) {
      facet.decode(facesContext);
    }
*/
  }

  private void decodeColumnAction(final FacesContext facesContext, final List<AbstractUIColumnBase> columns) {
    for (final AbstractUIColumnBase column : columns) {
      final boolean sortable = ComponentUtils.getBooleanAttribute(column, Attributes.sortable);
      if (sortable) {
        final String sourceId = facesContext.getExternalContext().getRequestParameterMap()
            .get(ClientBehaviorContext.BEHAVIOR_SOURCE_PARAM_NAME);
        final String columnId = column.getClientId(facesContext);
        final String sorterId = columnId + "_" + AbstractUISheet.SORTER_ID;

        if (sorterId.equals(sourceId)) {
          final UIData data = (UIData) column.getParent();
          data.queueEvent(new SortActionEvent(data, column));
        }
      }
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final String sheetId = component.getClientId(facesContext);
    final Markup markup = component.getMarkup();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final PaginatorMode paginator = component.getPaginator();
    LOG.debug("paginator={}", paginator);

    switch (paginator) {
      case list, auto -> {
        final UIComponent after = ensureAfterFacetPaginator(facesContext, component);
        if (after.getChildCount() == 0) {
          final UIPaginatorList paginatorList = (UIPaginatorList) ComponentUtils.createComponent(
              facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "_paginator_list");
          after.getChildren().add(paginatorList);
        }
      }
      case page -> {
        final UIComponent after = ensureAfterFacetPaginator(facesContext, component);
        if (after.getChildCount() == 0) {
          final UIPaginatorPage paginatorPage = (UIPaginatorPage) ComponentUtils.createComponent(
              facesContext, Tags.paginatorPage.componentType(), RendererTypes.PaginatorPage, "_paginator_page");
          after.getChildren().add(paginatorPage);
        }
      }
      case row -> {
        final UIComponent after = ensureAfterFacetPaginator(facesContext, component);
        if (after.getChildCount() == 0) {
          final UIPaginatorRow paginatorRow = (UIPaginatorRow) ComponentUtils.createComponent(
              facesContext, Tags.paginatorRow.componentType(), RendererTypes.PaginatorRow, "_paginator_row");
          after.getChildren().add(paginatorRow);
        }
      }
      case useShowAttributes -> {
        boolean initialized = component.getAttributes().get("useShowAttributesInitialized") != null;

        if (!initialized) {
          final Map<ShowPosition, AbstractUIPaginator> paginatorMap = new HashMap<>();

          final ShowPosition rangePosition = component.getShowRowRange();
          if (!ShowPosition.none.equals(rangePosition) && component.isPagingVisible()) {
            final UIPaginatorRow paginatorRow = (UIPaginatorRow) ComponentUtils.createComponent(
                facesContext, Tags.paginatorRow.componentType(), RendererTypes.PaginatorRow, "_paginator_row");
            paginatorRow.setAlwaysVisible(component.isShowPagingAlways());
            paginatorMap.put(rangePosition, paginatorRow);
          }

          final ShowPosition listPosition = component.getShowDirectLinks();
          if (!ShowPosition.none.equals(listPosition) && component.isPagingVisible()) {
            final UIPaginatorList paginatorList = (UIPaginatorList) ComponentUtils.createComponent(
                facesContext, Tags.paginatorList.componentType(), RendererTypes.PaginatorList, "_paginator_list");
            paginatorList.setAlwaysVisible(component.isShowPagingAlways());
            paginatorList.setArrows(component.isShowDirectLinksArrows() ? Arrows.show : Arrows.hide);
            paginatorList.setMax(component.getDirectLinkCount());
            paginatorMap.put(listPosition, paginatorList);
          }

          final ShowPosition pagePosition = component.getShowPageRange();
          if (!ShowPosition.none.equals(pagePosition) && component.isPagingVisible()) {
            final UIPaginatorPage paginatorPage = (UIPaginatorPage) ComponentUtils.createComponent(
                facesContext, Tags.paginatorPage.componentType(), RendererTypes.PaginatorPage, "_paginator_page");
            paginatorPage.setAlwaysVisible(component.isShowPagingAlways());
            paginatorMap.put(pagePosition, paginatorPage);
          }

          if (!paginatorMap.isEmpty()) {
            final UIComponent after = ensureAfterFacetPaginator(facesContext, component);

            final ShowPosition[] order = {ShowPosition.left, ShowPosition.center, ShowPosition.right};
            for (ShowPosition showPosition : order) {
              if (paginatorMap.containsKey(showPosition)) {
                after.getChildren().add(paginatorMap.get(showPosition));
              } else {
                final UIOut space = (UIOut) ComponentUtils.createComponent(
                    facesContext, Tags.out.componentType(), RendererTypes.Out, "_space_" + showPosition.name());
                after.getChildren().add(space);
              }
            }
          }

          component.getAttributes().put("useShowAttributesInitialized", Boolean.TRUE);
        }
      }
      case custom -> {
        // nothing to do
      }
      default -> throw new IllegalStateException("Unknown paginator mode: " + paginator);
    }

    UIComponent header = component.getHeader();
    if (header == null) {
      header = ComponentUtils.createComponent(facesContext, Tags.panel.componentType(), null, "_header");
      header.setTransient(true);
      final List<AbstractUIColumnBase> columns = component.getAllColumns();
      int i = 0;
      for (final AbstractUIColumnBase column : columns) {
        if (!(column instanceof AbstractUIRow)) {
          final UIComponent labelFacet = ComponentUtils.getFacet(column, Facets.label);
          final AbstractUIOut out = (AbstractUIOut) ComponentUtils.createComponent(
              facesContext, Tags.out.componentType(), RendererTypes.Out, "_col" + i);
          out.setTransient(true);
          if (labelFacet == null) {
            out.setValue(ComponentUtils.getAttribute(column, Attributes.label));
          }
          out.setRendered(column.isRendered());
          out.setLabelLayout(LabelLayout.skip);
          header.getChildren().add(out);
        }
        i++;
      }
      component.setHeader(header);
    }
    component.init(facesContext);

    // Outer sheet div
    insideBegin(facesContext, HtmlElements.TOBAGO_SHEET);
    writer.startElement(HtmlElements.TOBAGO_SHEET);
    writer.writeIdAttribute(sheetId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeClassAttribute(
        component.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    writer.writeAttribute(DataAttributes.SELECTION_MODE, component.getSelectable().name(), false);
    writer.writeAttribute(DataAttributes.FIRST, Integer.toString(component.getFirst()), false);
    writer.writeAttribute(CustomAttributes.ROWS, component.getRows());
    writer.writeAttribute(CustomAttributes.ROW_COUNT, Integer.toString(component.getRowCount()), false);
    writer.writeAttribute(CustomAttributes.LAZY, component.isLazy());
    writer.writeAttribute(CustomAttributes.LAZY_ROWS, component.isLazy() ? component.getLazyRows() : null);
    writer.writeAttribute(CustomAttributes.LAZY_UPDATE, component.getLazyUpdate());

    final boolean autoLayout = component.isAutoLayout();
    if (!autoLayout) {
      writer.writeAttribute(DataAttributes.LAYOUT, JsonUtils.encode(component.getColumnLayout(), "columns"), true);
    }

    encodeBehavior(writer, facesContext, component);

    final AbstractUIReload reload = ComponentUtils.getReloadFacet(component);
    if (reload != null) {
      reload.encodeAll(facesContext);
    }
  }

  private static <T extends AbstractUISheet> UIComponent ensureAfterFacetPaginator(
      final FacesContext facesContext, final T component) {
    UIComponent after = component.getFacet(Facets.AFTER);
    if (after == null) {
      after = ComponentUtils.createComponent(
          facesContext, Tags.paginatorPanel.componentType(), RendererTypes.PaginatorPanel, "_after");
      component.getFacets().put(Facets.AFTER, after);
    }
    return after;
  }

  @Override
  public void encodeChildrenInternal(final FacesContext facesContext, final T component) throws IOException {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof AbstractUIStyle) {
        child.encodeAll(facesContext);
      }
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String sheetId = component.getClientId(facesContext);
    final Selectable selectable = component.getSelectable();
    final SheetState state = component.getSheetState(facesContext);
    final List<Integer> columnWidths = component.getState().getColumnWidths();
    final boolean definedColumnWidths = component.getState().isDefinedColumnWidths();
    final List<Integer> selectedRows = getSelectedRows(component, state);
    final List<AbstractUIColumnBase> columns = component.getAllColumns();
    final boolean autoLayout = component.isAutoLayout();

    ensureColumnWidthsSize(columnWidths, columns, Collections.emptyList());

    if (!autoLayout) {
      encodeHiddenInput(writer,
          JsonUtils.encode(definedColumnWidths ? columnWidths : Collections.emptyList()),
          sheetId + SUFFIX_WIDTHS);

      final ArrayList<Boolean> encodedRendered = new ArrayList<>();
      for (final AbstractUIColumnBase column : columns) {
        if (!(column instanceof AbstractUIRow) && !(column instanceof AbstractUIColumnPanel)) {
          encodedRendered.add(column.isRendered());
        }
      }

      encodeHiddenInput(writer,
          JsonUtils.encode(encodedRendered.toArray(new Boolean[0])),
          sheetId + SUFFIX_COLUMN_RENDERED);
    }

    encodeHiddenInput(writer,
        component.getState().getScrollPosition().encode(),
        component.getClientId(facesContext) + SUFFIX_SCROLL_POSITION);

    encodeHiddenInput(writer,
        JsonUtils.encode(selectedRows),
        sheetId + SUFFIX_SELECTED);

    if (component.isLazy()) {
      encodeHiddenInput(writer, null, sheetId + SUFFIX_LAZY);
      encodeHiddenInput(writer, state.getLazyScrollPosition().encode(), sheetId + SUFFIX_LAZY_SCROLL_POSITION);
    }

    final List<Integer> expandedValue = component.isTreeModel() ? new ArrayList<>() : null;

    encodeTableBody(
        facesContext, component, writer, sheetId, selectable, columnWidths, selectedRows, columns, autoLayout,
        expandedValue);

    if (component.isPagingVisible()) {
      writer.startElement(HtmlElements.FOOTER);
      writer.endElement(HtmlElements.FOOTER);
    }

    final UIComponent after = component.getFacet(Facets.AFTER);
    if (after != null) {
      after.encodeAll(facesContext);
    }

    if (component.isTreeModel()) {
      writer.startElement(HtmlElements.INPUT);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
      final String expandedId = sheetId + ComponentUtils.SUB_SEPARATOR + AbstractUIData.SUFFIX_EXPANDED;
      writer.writeNameAttribute(expandedId);
      writer.writeIdAttribute(expandedId);
      writer.writeClassAttribute(TobagoClass.EXPANDED);
      writer.writeAttribute(HtmlAttributes.VALUE, JsonUtils.encode(expandedValue), false);
      writer.endElement(HtmlElements.INPUT);
    }

    writer.endElement(HtmlElements.TOBAGO_SHEET);
    UIComponent header = component.getHeader();
    if (header.isTransient()) {
      component.getFacets().remove("header");
    }
    insideEnd(facesContext, HtmlElements.TOBAGO_SHEET);
  }

  private void encodeTableBody(
      final FacesContext facesContext, final AbstractUISheet sheet,
      final TobagoResponseWriter writer, final String sheetId, final Selectable selectable,
      final List<Integer> columnWidths, final List<Integer> selectedRows, final List<AbstractUIColumnBase> columns,
      final boolean autoLayout, final List<Integer> expandedValue) throws IOException {

    final boolean showHeader = sheet.isShowHeader();
    final Markup sheetMarkup = sheet.getMarkup() != null ? sheet.getMarkup() : Markup.NULL;

    boolean nonLazyUpdate = !sheet.isLazyUpdate(facesContext);
    if (nonLazyUpdate) {
      final UIComponent before = sheet.getFacet(Facets.BEFORE);
      if (before != null) {
        before.encodeAll(facesContext);
      }

      if (showHeader && !autoLayout) {
        // if no autoLayout, we render the header in a separate table.
        writer.startElement(HtmlElements.HEADER);
        writer.startElement(HtmlElements.TABLE);
        writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", false);
        writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", false);
        writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
        writer.writeClassAttribute(
            BootstrapClass.TABLE,
            sheetMarkup.contains(Markup.DARK) ? BootstrapClass.TABLE_DARK : null,
            sheetMarkup.contains(Markup.BORDERED) ? BootstrapClass.TABLE_BORDERED : null,
            sheetMarkup.contains(Markup.SMALL) ? BootstrapClass.TABLE_SM : null,
            TobagoClass.TABLE_LAYOUT__FIXED);

        writeColgroup(facesContext, sheet, writer, columnWidths, columns, true);
        writer.startElement(HtmlElements.THEAD);
        encodeHeaderRows(facesContext, sheet, writer, columns);
        writer.endElement(HtmlElements.THEAD);
        writer.endElement(HtmlElements.TABLE);

        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(TobagoClass.SCROLLBAR__FILLER,
            BootstrapClass.TABLE,
            sheetMarkup.contains(Markup.DARK) ? BootstrapClass.TABLE_DARK : null,
            sheetMarkup.contains(Markup.BORDERED) ? BootstrapClass.TABLE_BORDERED : null);
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.HEADER);
      }
    }
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.BODY);

    writer.startElement(HtmlElements.TABLE);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", false);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", false);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    writer.writeClassAttribute(
        BootstrapClass.TABLE,
        sheetMarkup.contains(Markup.DARK) ? BootstrapClass.TABLE_DARK : null,
        sheetMarkup.contains(Markup.STRIPED) ? BootstrapClass.TABLE_STRIPED : null,
        sheetMarkup.contains(Markup.BORDERED) ? BootstrapClass.TABLE_BORDERED : null,
        sheetMarkup.contains(Markup.HOVER) ? BootstrapClass.TABLE_HOVER : null,
        sheetMarkup.contains(Markup.SMALL) ? BootstrapClass.TABLE_SM : null,
        !autoLayout ? TobagoClass.TABLE_LAYOUT__FIXED : null);

    if (nonLazyUpdate) {
      if (showHeader && autoLayout) {
        writer.startElement(HtmlElements.THEAD);
        encodeHeaderRows(facesContext, sheet, writer, columns);
        writer.endElement(HtmlElements.THEAD);
      }
      if (!autoLayout) {
        writeColgroup(facesContext, sheet, writer, columnWidths, columns, false);
      }
    }
    // Print the Content
    if (LOG.isDebugEnabled()) {
      LOG.debug("first = " + sheet.getFirst() + "   rows = " + sheet.getRows());
    }
    writer.startElement(HtmlElements.TBODY);

    final String var = sheet.getVar();

    boolean emptySheet = true;
    // rows = 0 means: show all
    final int first = sheet.isLazy() ? sheet.getLazyFirstRow() : sheet.getFirst();
    final int last = sheet.isLazy() ? sheet.getLazyFirstRow() + sheet.getLazyRows()
        : sheet.isRowsUnlimited() ? Integer.MAX_VALUE : sheet.getFirst() + sheet.getRows();

    AbstractUIRow row = null;
    boolean[] columnRendered = new boolean[columns.size()];
    for (int i = 0; i < columns.size(); i++) {
      UIColumn column = columns.get(i);
      if (column.isRendered()) {
        columnRendered[i] = true;
        if (column instanceof AbstractUIRow) {
          row = (AbstractUIRow) column;
          // todo: Markup.CLICKABLE ???
        }
      }
    }

    for (int rowIndex = first; rowIndex < last; rowIndex++) {
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

      if (sheet.isTreeModel() && sheet.isFolder() && sheet.getExpandedState().isExpanded(sheet.getPath())) {
        expandedValue.add(rowIndex);
      }

      writer.startElement(HtmlElements.TR);
      writer.writeAttribute(CustomAttributes.ROW_INDEX, rowIndex);
      final boolean selected = selectedRows.contains(rowIndex);
      final String parentId = sheet.getRowParentClientId();
      if (parentId != null) {
        // TODO: replace with writer.writeIdAttribute(parentId + SUB_SEPARATOR + AbstractUITree.SUFFIX_PARENT);
        // todo like in TreeListboxRenderer
        writer.writeAttribute(DataAttributes.TREE_PARENT, parentId, false);
      }
      // the row client id depends on the existence of an UIRow component! TBD: is this good?
      writer.writeIdAttribute(row != null ? row.getClientId(facesContext) : sheet.getRowClientId());
      writer.writeClassAttribute(
          selected ? TobagoClass.SELECTED : null,
          selected ? BootstrapClass.TABLE_INFO : null,
          row != null ? row.getCustomClass() : null,
          sheet.isRowVisible() ? null : BootstrapClass.D_NONE);

      int colSpan = 0;
      AbstractUIColumnPanel panel = null;

      for (int i = 0; i < columns.size(); i++) {
        AbstractUIColumnBase column = columns.get(i);
        if (columnRendered[i]) {
          if (column instanceof AbstractUIColumn || column instanceof AbstractUIColumnSelector
              || column instanceof AbstractUIColumnNode) {
            colSpan++;
            writer.startElement(HtmlElements.TD);
            Markup markup = column.getMarkup();
            if (markup == null) {
              markup = Markup.NULL;
            }
            writer.writeClassAttribute(
                BootstrapClass.textAlign(
                    column instanceof AbstractUIColumn ? ((AbstractUIColumn) column).getAlign() : null),
                BootstrapClass.verticalAlign(
                    column instanceof AbstractUIColumn ? ((AbstractUIColumn) column).getVerticalAlign() : null),
                column.getCustomClass());

            if (column instanceof AbstractUIColumnSelector selector) {
              writer.startElement(HtmlElements.INPUT);
              writer.writeNameAttribute(sheetId + "_data_row_selector_" + rowIndex);
              Selectable currentSelectable = getSelectionMode(selectable, selector);
              if (currentSelectable.isSingle()) {
                writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO);
              } else {
                writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
              }
              writer.writeAttribute(HtmlAttributes.TITLE,
                  ResourceUtils.getString(facesContext, "sheet.selectRow"), true);
              writer.writeAttribute(Arias.LABEL,
                  ResourceUtils.getString(facesContext, "sheet.selectRow"), true);
              writer.writeAttribute(HtmlAttributes.CHECKED, selected);
              writer.writeAttribute(HtmlAttributes.DISABLED, selector.isDisabled());
              writer.writeClassAttribute(
                  BootstrapClass.FORM_CHECK_INLINE,
                  TobagoClass.SELECTED);
              writer.endElement(HtmlElements.INPUT);
            } else if (column instanceof AbstractUIColumnNode) {
              column.encodeAll(facesContext);
            } else {
              final int childCount = column.getChildCount();
              if (childCount > 0) {
                final List<UIComponent> children = column.getChildren();
                for (int k = 0; k < childCount; k++) {
                  children.get(k).encodeAll(facesContext);
                }
              }
            }
            writer.endElement(HtmlElements.TD);
          } else if (column instanceof AbstractUIColumnPanel) {
            panel = (AbstractUIColumnPanel) column;
          }
        }
      }

      if (!autoLayout) {
        writer.startElement(HtmlElements.TD);
        writer.writeClassAttribute(TobagoClass.ROW__FILLER);
        writer.endElement(HtmlElements.TD);
        colSpan++;
      }

      writer.startElement(HtmlElements.TD);
      writer.writeClassAttribute(TobagoClass.BEHAVIOR__CONTAINER);
      encodeBehavior(writer, facesContext, row);
      writer.endElement(HtmlElements.TD);
      writer.endElement(HtmlElements.TR);

      if (panel != null) {
        writer.startElement(HtmlElements.TR);
        writer.writeNameAttribute(String.valueOf(rowIndex));
        writer.writeClassAttribute(TobagoClass.COLUMN__PANEL, panel.getCustomClass());
        writer.startElement(HtmlElements.TD);
        writer.writeAttribute(HtmlAttributes.COLSPAN, colSpan);
        panel.encodeAll(facesContext);
        writer.endElement(HtmlElements.TD);
        writer.endElement(HtmlElements.TR);
      }
    }
    sheet.setRowIndex(-1);

    if (emptySheet && showHeader) {
      writer.startElement(HtmlElements.TR);
      int countColumns = 0;
      for (final UIColumn column : columns) {
        if (!(column instanceof AbstractUIRow)) {
          countColumns++;
        }
      }
      writer.startElement(HtmlElements.TD);
      writer.writeAttribute(HtmlAttributes.COLSPAN, countColumns);
      final UIComponent emptyFacet = ComponentUtils.getFacet(sheet, Facets.empty);
      if (emptyFacet != null) {
        emptyFacet.encodeAll(facesContext);
      } else {
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(BootstrapClass.TEXT_CENTER);
        writer.writeText(ResourceUtils.getString(facesContext, "sheet.empty"));
        writer.endElement(HtmlElements.DIV);
      }
      writer.endElement(HtmlElements.TD);
      if (!autoLayout) {
        writer.startElement(HtmlElements.TD);
        writer.startElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.TD);
      }
      writer.endElement(HtmlElements.TR);
    }

    writer.endElement(HtmlElements.TBODY);
    writer.endElement(HtmlElements.TABLE);
    writer.endElement(HtmlElements.DIV);
  }

  private static Selectable getSelectionMode(Selectable selectable, AbstractUIColumnSelector selector) {
    if (selectable == Selectable.none) {
      Selectable selectorSelectable = selector.getSelectable();
      if (selectorSelectable != null) {
        return selectorSelectable;
      }
    }
    return selectable;
  }

  private void encodeHiddenInput(final TobagoResponseWriter writer, final String value, final String idWithSuffix)
      throws IOException {
    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(idWithSuffix);
    writer.writeNameAttribute(idWithSuffix);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.VALUE, value, false);
    writer.endElement(HtmlElements.INPUT);
  }

  private void ensureColumnWidthsSize(
      final List<Integer> columnWidths, final List<AbstractUIColumnBase> columns, final List<Integer> samples) {
    // we have to fill the non rendered positions with some values.
    // on client site, we don't know nothing about the non-rendered columns.
    int i = 0;
    int j = 0;
    for (final AbstractUIColumnBase column : columns) {
      if (!(column instanceof AbstractUIRow)) {
        final Integer newValue;
        if (j < samples.size()) {
          newValue = samples.get(j);
          j++;
        } else {
          newValue = null;
        }
        if (columnWidths.size() > i) {
          if (newValue != null) {
            columnWidths.set(i, newValue);
          }
        } else {
          columnWidths.add(newValue != null ? newValue : -1); // -1 means unknown or undefined
        }
        i++;
      }
    }
  }

  private void encodeHeaderRows(
      final FacesContext facesContext, final AbstractUISheet sheet, final TobagoResponseWriter writer,
      final List<AbstractUIColumnBase> columns)
      throws IOException {

    final String sheetClientId = sheet.getClientId(facesContext);
    final Selectable selectable = sheet.getSelectable();
    final Grid grid = sheet.getHeaderGrid();
    final boolean autoLayout = sheet.isAutoLayout();
    final boolean multiHeader = grid.getRowCount() > 1;
    int offset = 0;

    for (int i = 0; i < grid.getRowCount(); i++) {
      writer.startElement(HtmlElements.TR);
      final AbstractUIRow row = ComponentUtils.findChild(sheet, AbstractUIRow.class);
      if (row != null) {
        writer.writeClassAttribute(row.getCustomClass());
      }
      for (int j = 0; j < columns.size(); j++) {
        final AbstractUIColumnBase column = columns.get(j);
        if (!column.isRendered() || column instanceof AbstractUIRow || column instanceof AbstractUIColumnPanel) {
          offset++;
        } else {
          final Cell cell = grid.getCell(j - offset, i);
          if (cell instanceof OriginCell) {
            writer.startElement(HtmlElements.TH);
            writer.writeIdAttribute(column.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + i);
            if (cell.getColumnSpan() > 1) {
              writer.writeAttribute(HtmlAttributes.COLSPAN, cell.getColumnSpan());
            }
            if (cell.getRowSpan() > 1) {
              writer.writeAttribute(HtmlAttributes.ROWSPAN, cell.getRowSpan());
            }
            final TextAlign align;
            final UIComponent cellComponent = cell.getComponent();
            if (multiHeader && cell.getColumnSpan() > 1) {
              align = TextAlign.center;
            } else if (column instanceof AbstractUIColumn) {
              align = ((AbstractUIColumn) column).getAlign();
            } else {
              align = null;
            }
            writer.writeClassAttribute(
                BootstrapClass.textAlign(align),
                column.getCustomClass());
            writer.startElement(HtmlElements.SPAN);
            boolean sortable = false;
            boolean ascending = false;
            boolean descending = false;
            CssItem sortPosition = null;
            String tip = ComponentUtils.getStringAttribute(column, Attributes.tip);
            // sorter icons should only displayed when there is only 1 column and not input
            CommandMap behaviorCommands = null;
            if (cell.getColumnSpan() == 1 && cellComponent instanceof AbstractUIOut) {
              sortable = ComponentUtils.getBooleanAttribute(column, Attributes.sortable);
              if (sortable) {
                AbstractUILink sortCommand = (AbstractUILink) ComponentUtils.getFacet(column, Facets.sorter);
                if (sortCommand == null) {
                  // assign id to column
                  column.getClientId(facesContext);
                  final String sorterId = column.getId() + "_" + AbstractUISheet.SORTER_ID;
                  sortCommand = (AbstractUILink) ComponentUtils.createComponent(
                      facesContext, Tags.link.componentType(), RendererTypes.Link, sorterId);
                  sortCommand.setTransient(true);
                  final AjaxBehavior reloadBehavior = sheet.createReloadBehavior(sheet);
                  sortCommand.addClientBehavior("click", reloadBehavior);
                  ComponentUtils.setFacet(column, Facets.sorter, sortCommand);
                }
                writer.writeIdAttribute(sortCommand.getClientId(facesContext));
                behaviorCommands = getBehaviorCommands(facesContext, sortCommand);
                ComponentUtils.removeFacet(column, Facets.sorter);
                if (tip == null) {
                  tip = "";
                } else {
                  tip += " - ";
                }
                tip += ResourceUtils.getString(facesContext, "sheet.sorting");

                final SheetState sheetState = sheet.getSheetState(facesContext);
                final SortedColumnList sortedColumnList = sheetState.getSortedColumnList();

                final int index = sortedColumnList.indexOf(column.getId());
                if (index >= 0) {
                  if (sortedColumnList.isShowNumbers()) { // ignore number circles otherwise
                    switch (index) {
                      case 0:
                        sortPosition = TobagoClass.CIRCLE__1;
                        break;
                      case 1:
                        sortPosition = TobagoClass.CIRCLE__2;
                        break;
                      case 2:
                        sortPosition = TobagoClass.CIRCLE__3;
                        break;
                      case 3:
                        sortPosition = TobagoClass.CIRCLE__4;
                        break;
                      case 4:
                        sortPosition = TobagoClass.CIRCLE__5;
                        break;
                      case 5:
                        sortPosition = TobagoClass.CIRCLE__6;
                        break;
                      case 6:
                        sortPosition = TobagoClass.CIRCLE__7;
                        break;
                      case 7:
                        sortPosition = TobagoClass.CIRCLE__8;
                        break;
                      case 8:
                        sortPosition = TobagoClass.CIRCLE__9;
                        break;
                      default:
                        // should not happen
                        LOG.warn("Index {} not supported!", index);
                    }
                  }

                  final SortedColumn sortedColumn = sortedColumnList.get(index);
                  if (Objects.equals(column.getId(), sortedColumn.getId())) {
                    final String sortTitle;
                    if (sortedColumn.isAscending()) {
                      sortTitle = ResourceUtils.getString(facesContext, "sheet.ascending");
                      ascending = true;
                    } else {
                      sortTitle = ResourceUtils.getString(facesContext, "sheet.descending");
                      descending = true;
                    }
                    tip += " - " + sortTitle;
                  }
                }
              }
            }

            writer.writeClassAttribute(
                sortable ? TobagoClass.SORTABLE : null,
                ascending ? TobagoClass.ASCENDING : null,
                descending ? TobagoClass.DESCENDING : null,
                sortPosition);
            writer.writeAttribute(HtmlAttributes.TITLE, tip, true);

            encodeBehavior(writer, behaviorCommands);
            if (column instanceof AbstractUIColumnSelector selector) {
              Selectable currentSelectable = getSelectionMode(selectable, selector);
              writer.startElement(HtmlElements.INPUT);
              if (currentSelectable.isMulti()) {
                writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
                writer.writeAttribute(HtmlAttributes.TITLE,
                    ResourceUtils.getString(facesContext, "sheet.selectAll"), true);
                writer.writeAttribute(Arias.LABEL,
                    ResourceUtils.getString(facesContext, "sheet.selectAll"), true);
                writer.writeAttribute(HtmlAttributes.DISABLED, selector.isDisabled());
              } else {
                writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
              }
              writer.writeNameAttribute(sheetClientId + SUFFIX_COLUMN_SELECTOR);
              writer.writeClassAttribute(TobagoClass.SELECTED);
              writer.writeAttribute(DataAttributes.SELECTION_MODE, currentSelectable.name(), false);
              writer.endElement(HtmlElements.INPUT);
            } else {
              if (ComponentUtils.getFacet(column, Facets.label) == null) {
                cellComponent.encodeAll(facesContext);
              }
            }

            final UIComponent label = ComponentUtils.getFacet(column, Facets.label);
            if (label != null) {
              insideBegin(facesContext, Facets.label);
              label.encodeAll(facesContext);
              insideEnd(facesContext, Facets.label);
            }

            writer.endElement(HtmlElements.SPAN);
            if (!autoLayout) {
              if (column.isResizable()) {
                encodeResizing(writer, sheet, j - offset + cell.getColumnSpan() - 1);
              }
            }

            final UIComponent bar = ComponentUtils.getFacet(column, Facets.bar);
            if (bar != null) {
              insideBegin(facesContext, Facets.bar);
              bar.encodeAll(facesContext);
              insideEnd(facesContext, Facets.bar);
            }

            writer.endElement(HtmlElements.TH);
          }
        }
      }
      if (!autoLayout) {
        encodeHeaderFiller(writer, TobagoClass.ROW__FILLER);
      }
      encodeHeaderFiller(writer, TobagoClass.BEHAVIOR__CONTAINER);

      writer.endElement(HtmlElements.TR);
    }
  }

  private void encodeHeaderFiller(final TobagoResponseWriter writer, final CssItem cssItem) throws IOException {
    writer.startElement(HtmlElements.TH);
    writer.writeClassAttribute(cssItem);
    writer.startElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.TH);
  }

  private void writeColgroup(
      final FacesContext facesContext, final AbstractUISheet sheet, final TobagoResponseWriter writer,
      final List<Integer> columnWidths, final List<AbstractUIColumnBase> columns, final boolean isHeader)
      throws IOException {

    final boolean columnWidthSetByUser = columnWidths.stream().noneMatch(columnWidth -> columnWidth <= -1);

    writer.startElement(HtmlElements.COLGROUP);
    int numOfCols = 0;
    for (final AbstractUIColumnBase column : columns) {
      if (!(column instanceof AbstractUIRow) && !(column instanceof AbstractUIColumnPanel)) {
        if (column.isRendered()) {
          writeCol(writer, null);
        }
        numOfCols++;
      }
    }
    writeCol(writer, TobagoClass.ROW__FILLER);
    writeCol(writer, TobagoClass.BEHAVIOR__CONTAINER);
    writer.endElement(HtmlElements.COLGROUP);

    if (isHeader) { // write style tag only once (when rendering header), but for both header and body
      final MeasureList columnLayout = sheet.getColumnLayout();
      final String sheetId = sheet.getClientId(facesContext);
      final String encodedSheetId = StyleRenderUtils.encodeIdSelector(sheetId);
      float fr = 0;
      MeasureList tangibleMeasures = new MeasureList();
      for (int colIndex = 0; colIndex < numOfCols; colIndex++) {
        Measure measure;
        if (columnWidthSetByUser) {
          measure = new Measure(columnWidths.get(colIndex), Measure.Unit.PX);
        } else {
          measure = columnLayout.get(colIndex % columnLayout.getSize());
        }
        if (Measure.Unit.FR.equals(measure.getUnit())) {
          fr += measure.getValue();
        } else {
          if (Measure.Unit.AUTO.equals(measure.getUnit())) {
            tangibleMeasures.add(new Measure(100.0 / (numOfCols + 2), Measure.Unit.PERCENT));
            /* (numOfCols + 2) for backwards compatibility, because the
            previous initialization in TypeScript had included the COL.tobago-row-filler and
            COL.tobago-behavior-container in the calculation. */
          } else {
            tangibleMeasures.add(measure);
          }
          encodeColStyle(facesContext, encodedSheetId, null, colIndex, measure);
        }
      }

      if (fr > 0) {
        for (int colIndex = 0; colIndex < numOfCols; colIndex++) {
          Measure measure = columnLayout.get(colIndex % columnLayout.getSize());
          if (Measure.Unit.FR.equals(measure.getUnit())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("calc((100%");
            for (Measure tangibleMeasure : tangibleMeasures) {
              stringBuilder.append(" - ");
              stringBuilder.append(tangibleMeasure.getValue());
              stringBuilder.append(tangibleMeasure.getUnit().getValue());
            }
            stringBuilder.append(") / ");
            stringBuilder.append(fr);
            stringBuilder.append(" * ");
            stringBuilder.append(measure.getValue());
            stringBuilder.append(")");
            encodeColStyle(facesContext, writer, encodedSheetId, colIndex, stringBuilder.toString());
          }
        }
      }

      Measure zeroPixel = new Measure(0, Measure.Unit.PX);
      encodeColStyle(facesContext, encodedSheetId, TobagoClass.ROW__FILLER, null,
          columnWidthSetByUser ? Measure.AUTO : zeroPixel);
      encodeColStyle(facesContext, encodedSheetId, TobagoClass.BEHAVIOR__CONTAINER, null, zeroPixel);
    }
  }

  private void writeCol(final TobagoResponseWriter writer, final CssItem cssItem) throws IOException {
    writer.startElement(HtmlElements.COL);
    writer.writeClassAttribute(cssItem);
    writer.endElement(HtmlElements.COL);
  }

  private void encodeColStyle(
      final FacesContext facesContext, final String encodedSheetId, final CssItem cssItem, final Integer colIndex,
      final Measure measure) throws IOException {
    final AbstractUIStyle style = (AbstractUIStyle) facesContext.getApplication().createComponent(
        facesContext, Tags.style.componentType(), RendererTypes.Style.name());
    style.setTransient(true);

    StringBuilder stringBuilder = new StringBuilder();

    if (cssItem != null) {
      stringBuilder.append(encodedSheetId);
      stringBuilder.append(" > header > table > colgroup > col.");
      stringBuilder.append(cssItem.getName());
      stringBuilder.append(", ");
      stringBuilder.append(encodedSheetId);
      stringBuilder.append(" > .tobago-body > table > colgroup > col.");
      stringBuilder.append(cssItem.getName());
    } else {
      stringBuilder.append(encodedSheetId);
      stringBuilder.append(" > header > table > colgroup > col:nth-child(");
      stringBuilder.append(colIndex + 1);
      stringBuilder.append(")");
      stringBuilder.append(", ");
      stringBuilder.append(encodedSheetId);
      stringBuilder.append(" > .tobago-body > table > colgroup > col:nth-child(");
      stringBuilder.append(colIndex + 1);
      stringBuilder.append(")");
    }
    style.setSelector(stringBuilder.toString());
    style.setWidth(measure);
    style.encodeAll(facesContext);
  }

  private void encodeColStyle(
      final FacesContext facesContext, final TobagoResponseWriter writer,
      final String encodedSheetId, final Integer colIndex, final String widthCalc) throws IOException {
    writer.startElement(HtmlElements.STYLE);
    writer.writeAttribute(HtmlAttributes.NONCE, Nonce.getNonce(facesContext), false);

    String selector = encodedSheetId
        + " > header > table > colgroup > col:nth-child("
        + (colIndex + 1)
        + ")"
        + ", "
        + encodedSheetId
        + " > .tobago-body > table > colgroup > col:nth-child("
        + (colIndex + 1)
        + ")";
    StyleRenderUtils.writeSelector(writer, selector);

    writer.writeText("{width:");
    writer.writeText(widthCalc);
    writer.writeText(";}");

    writer.endElement(HtmlElements.STYLE);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  private List<Integer> getSelectedRows(final AbstractUISheet data, final SheetState state) {
    List<Integer> selected = (List<Integer>) ComponentUtils.getAttribute(data, Attributes.selectedListString);
    if (selected == null && state != null) {
      selected = state.getSelectedRows();
    }
    if (selected == null) {
      selected = Collections.emptyList();
    }
    return selected;
  }

  private void encodeResizing(final TobagoResponseWriter writer, final AbstractUISheet sheet, final int columnIndex)
      throws IOException {
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.RESIZE);
    writer.writeAttribute(DataAttributes.COLUMN_INDEX, Integer.toString(columnIndex), false);
    writer.write("&nbsp;&nbsp;"); // is needed for IE
    writer.endElement(HtmlElements.SPAN);
  }
}
