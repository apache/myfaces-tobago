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

package org.apache.myfaces.tobago.internal.component;

import jakarta.el.ELContext;
import jakarta.el.MethodExpression;
import jakarta.el.ValueExpression;
import jakarta.faces.component.UIColumn;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ComponentSystemEventListener;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PreRenderComponentEvent;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeListener;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.internal.util.SortingUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.layout.ShowPosition;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.ScrollPosition;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.SheetTagDeclaration}
 */
@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUISheet extends AbstractUIData
    implements SheetStateChangeSource, SortActionSource, ClientBehaviorHolder, Visual,
    ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * @deprecated since 4.4.0.
   */
  @Deprecated
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  public static final String SORTER_ID = "sorter";
  public static final String NOT_SORTABLE_COL_MESSAGE_ID = "org.apache.myfaces.tobago.UISheet.SORTING_COL";
  public static final String NOT_SORTABLE_MESSAGE_ID = "org.apache.myfaces.tobago.UISheet.SORTING";

  private SheetState state;
  private transient MeasureList columnLayout;
  private transient boolean autoLayout;

  private transient Grid headerGrid;

  @Override
  public void encodeAll(FacesContext facesContext) throws IOException {

    if (isLazy()) {
      if (getRows() == 0) {
        LOG.warn("Sheet id={} has lazy=true set, but not set the rows attribute!", getClientId(facesContext));
      }
      if (getShowRowRange() != ShowPosition.none) {
        LOG.warn("Sheet id={} has lazy=true set, but also set showRowRange!=none!", getClientId(facesContext));
      }
      if (getShowPageRange() != ShowPosition.none) {
        LOG.warn("Sheet id={} has lazy=true set, but also set showPageRange!=none!", getClientId(facesContext));
      }
      if (getShowDirectLinks() != ShowPosition.none) {
        LOG.warn("Sheet id={} has lazy=true set, but also set showDirectLinks!=none!", getClientId(facesContext));
      }
    }

    super.encodeAll(facesContext);
  }

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    final SheetState theState = getSheetState(facesContext);
    final int first = theState.getFirst();
    if (first > -1 && (!hasRowCount() || first < getRowCount())) {
      final ValueExpression expression = getValueExpression(Attributes.first.getName());
      if (expression != null) {
        expression.setValue(facesContext.getELContext(), first);
      } else {
        setFirst(first);
      }
    }

    super.encodeBegin(facesContext);
  }

  public void setState(final SheetState state) {
    this.state = state;
  }

  public SheetState getState() {
    return getSheetState(FacesContext.getCurrentInstance());
  }

  public SheetState getSheetState(final FacesContext facesContext) {
    if (state != null) {
      return state;
    }

    final ValueExpression expression = getValueExpression(Attributes.state.getName());
    if (expression != null) {
      final ELContext elContext = facesContext.getELContext();
      SheetState sheetState = (SheetState) expression.getValue(elContext);
      if (sheetState == null) {
        sheetState = new SheetState(getMaxSortColumns());
        expression.setValue(elContext, sheetState);
      }
      return sheetState;
    }

    state = new SheetState(getMaxSortColumns());
    return state;
  }

  public abstract String getColumns();

  @Override
  public void processEvent(final ComponentSystemEvent event) throws AbortProcessingException {

    super.processEvent(event);

    if (event instanceof PreRenderComponentEvent) {
      final String columns = getColumns();
      if (columns != null) {
        columnLayout = MeasureList.parse(columns);
      }

      autoLayout = true;
      if (columnLayout != null) {
        for (final Measure token : columnLayout) {
          if (token != Measure.AUTO) {
            autoLayout = false;
            break;
          }
        }
      }

      LOG.debug("autoLayout={}", autoLayout);
    }
  }

  public MeasureList getColumnLayout() {
    return columnLayout;
  }

  public boolean isAutoLayout() {
    return autoLayout;
  }

  /**
   * The rowIndex of the last row on the current page plus one (because of zero based iterating).
   *
   * @throws IllegalArgumentException If the number of rows in the model returned by {@link #getRowCount()} is -1
   *                                  (undefined).
   */
  public int getLastRowIndexOfCurrentPage() {
    if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the last row, because the row count of the model is unknown.");
    }
    if (isRowsUnlimited()) {
      return getRowCount();
    }
    final int last = getFirst() + getRows();
    return Math.min(last, getRowCount());
  }

  /**
   * @return returns the current page (based by 0).
   */
  public int getCurrentPage() {
    final int rows = getRows();
    if (rows == 0) {
      // if the rows are unlimited, there is only one page
      return 0;
    }
    final int first = getFirst();
    if (hasRowCount() && first >= getRowCount()) {
      return getPages() - 1; // last page
    } else {
      return first / rows;
    }
  }

  /**
   * The number of pages to render.
   *
   * @throws IllegalArgumentException If the number of rows in the model returned by {@link #getRowCount()} is -1
   *                                  (undefined).
   */
  public int getPages() {
    if (isRowsUnlimited()) {
      return 1;
    }
    if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the number of pages, because the row count of the model is unknown.");
    }
    return (getRowCount() - 1) / getRows() + 1;
  }

  public List<UIComponent> getRenderedChildrenOf(final UIColumn column) {
    final List<UIComponent> children = new ArrayList<>();
    for (final UIComponent kid : column.getChildren()) {
      if (kid.isRendered()) {
        children.add(kid);
      }
    }
    return children;
  }

  /**
   * @return Is the interval to display starting with the first row?
   */
  public boolean isAtBeginning() {
    return getFirst() == 0;
  }

  /**
   * @return Does the data model knows the number of rows?
   */
  public boolean hasRowCount() {
    return getRowCount() != -1;
  }

  /**
   * @return Should the paging controls be rendered? Either because of the need of paging or because the show is
   * enforced by {@link #isShowPagingAlways()}
   */
  public boolean isPagingVisible() {
    return isShowPagingAlways() || needMoreThanOnePage();
  }

  /**
   * @return Is panging needed to display all rows? If the number of rows is unknown this method returns true.
   */
  public boolean needMoreThanOnePage() {
    if (isRowsUnlimited()) {
      return false;
    } else if (!hasRowCount()) {
      return true;
    } else {
      return getRowCount() > getRows();
    }
  }

  public abstract boolean isShowPagingAlways();

  public boolean isAtEnd() {
    if (!hasRowCount()) {
      final int old = getRowIndex();
      setRowIndex(getFirst() + getRows() + 1);
      final boolean atEnd = !isRowAvailable();
      setRowIndex(old);
      return atEnd;
    } else {
      return getFirst() >= getFirstRowIndexOfLastPage();
    }
  }

  /**
   * Determines the beginning of the last page in the model. If the number of rows to display on one page is unlimited,
   * the value is 0 (there is only one page).
   *
   * @return The index of the first row of the last paging page.
   * @throws IllegalArgumentException If the number of rows in the model returned by {@link #getRowCount()} is -1
   *                                  (undefined).
   */
  public int getFirstRowIndexOfLastPage() {
    if (isRowsUnlimited()) {
      return 0;
    } else if (!hasRowCount()) {
      throw new IllegalArgumentException(
          "Can't determine the last page, because the row count of the model is unknown.");
    } else {
      final int rows = getRows();
      final int rowCount = getRowCount();
      final int tail = rowCount % rows;
      return rowCount - (tail != 0 ? tail : rows);
    }
  }

  @Override
  public void processUpdates(final FacesContext context) {
    super.processUpdates(context);

    final SheetState sheetState = getSheetState(context);
    if (sheetState != null) {
      final List<Integer> list = (List<Integer>) ComponentUtils.getAttribute(this, Attributes.selectedListString);
      sheetState.setSelectedRows(list != null ? list : Collections.emptyList());
      ComponentUtils.removeAttribute(this, Attributes.selectedListString);
      ComponentUtils.removeAttribute(this, Attributes.scrollPosition);
    }
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] saveState = new Object[2];
    saveState[0] = super.saveState(context);
    saveState[1] = state;
    return saveState;
  }

  @Override
  public void restoreState(final FacesContext context, final Object savedState) {
    final Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    state = (SheetState) values[1];
  }

  public List<AbstractUIColumnBase> getAllColumns() {
    final ArrayList<AbstractUIColumnBase> result = new ArrayList<>();
    findColumns(this, result, true);
    return result;
  }

  private void findColumns(final UIComponent component, final List<AbstractUIColumnBase> result, final boolean all) {
    for (final UIComponent child : component.getChildren()) {
      if (all || child.isRendered()) {
        if (child instanceof AbstractUIColumnBase) {
          result.add((AbstractUIColumnBase) child);
        } else if (child instanceof AbstractUIData) {
          // ignore columns of nested sheets
        } else {
          findColumns(child, result, all);
        }
      }
    }
  }

  @Override
  public void queueEvent(final FacesEvent facesEvent) {
    final UIComponent parent = getParent();
    if (parent == null) {
      throw new IllegalStateException("Component is not a descendant of a UIViewRoot");
    }

    if (facesEvent.getComponent() == this
        && (facesEvent instanceof SheetStateChangeEvent
        || facesEvent instanceof PageActionEvent)) {
      facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      parent.queueEvent(facesEvent);
    } else {
      super.queueEvent(facesEvent);
    }
  }

  @Override
  public void broadcast(final FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof SheetStateChangeEvent) {
      final MethodExpression listener = getStateChangeListenerExpression();
      listener.invoke(getFacesContext().getELContext(), new Object[]{facesEvent});
    } else if (facesEvent instanceof PageActionEvent) {
      if (facesEvent.getComponent() == this) {
        final MethodExpression listener = getStateChangeListenerExpression();
        if (listener != null) {
          listener.invoke(getFacesContext().getELContext(), new Object[]{facesEvent});
        }
        performPaging((PageActionEvent) facesEvent);
      }
    } else if (facesEvent instanceof SortActionEvent) {
      getSheetState(getFacesContext()).updateSortState(((SortActionEvent) facesEvent).getColumn().getId());
      sort(getFacesContext(), (SortActionEvent) facesEvent);
    }
  }

  public void init(final FacesContext facesContext) {
    sort(facesContext, null);
    layoutHeader();
  }

  private void layoutHeader() {
    final UIComponent header = getHeader();
    if (header == null) {
      LOG.warn("This should not happen. Please file a bug in the issue tracker to reproduce this case.");
      return;
    }
    final MeasureList tokens = new MeasureList();
    final List<AbstractUIColumnBase> columns = getAllColumns();
    for (final UIColumn column : columns) {
      if (!(column instanceof AbstractUIRow)) {
        tokens.add(Measure.FRACTION1);
      }
    }
    final MeasureList rows = new MeasureList();
    rows.add(Measure.AUTO);
    final Grid grid = new Grid(tokens, rows);

    for (final UIComponent child : header.getChildren()) {
      if (child.isRendered()) {
        final int columnSpan = ComponentUtils.getIntAttribute(child, Attributes.columnSpan, 1);
        final int rowSpan = ComponentUtils.getIntAttribute(child, Attributes.rowSpan, 1);
        grid.add(new OriginCell(child), columnSpan, rowSpan);
      }
    }
    setHeaderGrid(grid);
  }

  protected void sort(final FacesContext facesContext, final SortActionEvent event) {
    final SheetState sheetState = getSheetState(getFacesContext());
    if (sheetState.getToBeSortedLevel() > 0) {
      final MethodExpression expression = getSortActionListenerExpression();
      if (expression != null) {
        try {
          expression.invoke(facesContext.getELContext(),
              new Object[]{
                  event != null
                      ? event
                      : new SortActionEvent(this,
                      (UIColumn) findComponent(getSheetState(facesContext).getSortedColumnId()))});
        } catch (final Exception e) {
          LOG.warn("Sorting not possible!", e);
        }
      } else {
        SortingUtils.sort(this, null);
      }
      sheetState.sorted();
    }
  }

  @Override
  public void addStateChangeListener(final SheetStateChangeListener listener) {
    addFacesListener(listener);
  }

  @Override
  public SheetStateChangeListener[] getStateChangeListeners() {
    return (SheetStateChangeListener[]) getFacesListeners(SheetStateChangeListener.class);
  }

  @Override
  public void removeStateChangeListener(final SheetStateChangeListener listener) {
    removeFacesListener(listener);
  }

  public void performPaging(final PageActionEvent pageEvent) {

    int first;

    if (LOG.isDebugEnabled()) {
      LOG.debug("action = '" + pageEvent.getAction().name() + "'");
    }

    ScrollPosition scrollPosition = getState().getScrollPosition();
    scrollPosition.setTop(0);
    switch (pageEvent.getAction()) {
      case first:
        first = 0;
        break;
      case prev:
        first = getFirst() - getRows();
        first = Math.max(first, 0);
        scrollPosition.setTop(Integer.MAX_VALUE);
        break;
      case next:
        if (hasRowCount()) {
          first = getFirst() + getRows();
          first = first > getRowCount() ? getFirstRowIndexOfLastPage() : first;
        } else {
          if (isAtEnd()) {
            first = getFirst();
          } else {
            first = getFirst() + getRows();
          }
        }
        break;
      case last:
        first = getFirstRowIndexOfLastPage();
        break;
      case toRow:
      case lazy:
        first = pageEvent.getValue() - 1;
        if (hasRowCount() && first > getFirstRowIndexOfLastPage()) {
          first = getFirstRowIndexOfLastPage();
        } else if (first < 0) {
          first = 0;
        }
        break;
      case toPage:
        final int pageIndex = pageEvent.getValue() - 1;
        first = pageIndex * getRows();
        if (hasRowCount() && first > getFirstRowIndexOfLastPage()) {
          first = getFirstRowIndexOfLastPage();
        } else if (first < 0) {
          first = 0;
        }
        break;
      default:
        // may not happen
        first = -1;
    }

    final ValueExpression expression = getValueExpression(Attributes.first.getName());
    if (expression != null) {
      expression.setValue(getFacesContext().getELContext(), first);
    } else {
      setFirst(first);
    }

    getState().setFirst(first);
  }

  @Override
  public boolean isRendersRowContainer() {
    return true;
  }

  public abstract boolean isShowHeader();

  @Override
  public ExpandedState getExpandedState() {
    return getState().getExpandedState();
  }

  @Override
  public SelectedState getSelectedState() {
    return getState().getSelectedState();
  }

  public Grid getHeaderGrid() {
    return headerGrid;
  }

  public void setHeaderGrid(final Grid headerGrid) {
    this.headerGrid = headerGrid;
  }

  public abstract boolean isShowDirectLinksArrows();

  public abstract boolean isShowPageRangeArrows();

  public abstract ShowPosition getShowRowRange();

  public abstract ShowPosition getShowPageRange();

  public abstract ShowPosition getShowDirectLinks();

  public abstract boolean isLazy();

  public abstract Integer getMaxSortColumns();
}
