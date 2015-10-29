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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.Sorter;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeListener;
import org.apache.myfaces.tobago.event.SheetStateChangeSource2;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.SortActionSource2;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUISheet extends AbstractUIData
    implements SheetStateChangeSource2, SortActionSource2, OnComponentPopulated, SupportsRenderedPartially, Visual {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISheet.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  public static final String SORTER_ID = "sorter";

  private SheetState state;
  private List<Integer> widthList;
  private transient LayoutTokens columnLayout;

  private transient Grid headerGrid;

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    final SheetState state = getSheetState(facesContext);
    final int first = state.getFirst();
    if (first > -1 && (!hasRowCount() || first < getRowCount())) {
      final ValueExpression expression = getValueExpression(Attributes.FIRST);
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

    final ValueExpression expression = getValueExpression(Attributes.STATE);
    if (expression != null) {
      final ELContext elContext = facesContext.getELContext();
      SheetState sheetState = (SheetState) expression.getValue(elContext);
      if (sheetState == null) {
        sheetState = new SheetState();
        expression.setValue(elContext, sheetState);
      }
      return sheetState;
    }

    state = new SheetState();
    return state;
  }

  public abstract String getColumns();

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      final String columns = getColumns();
      if (columns != null) {
        columnLayout = LayoutTokens.parse(columns);
      }
    }
    return columnLayout;
  }

  /**
   * Remove the (by user) resized column widths. An application may provide a button to access it.
   * Since 1.0.26.
   */
  public void resetColumnWidths() {
    final SheetState state = getState();
    if (state != null) {
      state.setColumnWidths(null);
    }
    getAttributes().remove(Attributes.WIDTH_LIST_STRING);
  }

  /**
   * @deprecated The name of this method is ambiguous.
   * You may use {@link #getLastRowIndexOfCurrentPage()}. Deprecated since 1.5.5.
   */
  public int getLast() {
    final int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  /**
   * The rowIndex of the last row on the current page plus one (because of zero based iterating).
   * @throws IllegalArgumentException If the number of rows in the model returned
   * by {@link #getRowCount()} is -1 (undefined).
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
    return last < getRowCount() ? last : getRowCount();
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
      return (first / rows);
    }
  }
  
  /**
   * @return returns the current page (based by 1).
   * @deprecated Please use {@link #getCurrentPage()} which returns the value zero-based. Deprecated since 1.5.5.
   */
  @Deprecated
  public int getPage() {
    return getCurrentPage() + 1;
  }

  /**
   * The number of pages to render.
   * @throws IllegalArgumentException If the number of rows in the model returned
   * by {@link #getRowCount()} is -1 (undefined).
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
    final List<UIComponent> children = new ArrayList<UIComponent>();
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
   * @return Should the paging controls be rendered? Either because of the need of paging or because
   * the show is enforced by {@link #isShowPagingAlways()}
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
   * Determines the beginning of the last page in the model.
   * If the number of rows to display on one page is unlimited, the value is 0 (there is only one page).
   * @return The index of the first row of the last paging page.
   * @throws IllegalArgumentException If the number of rows in the model returned
   * by {@link #getRowCount()} is -1 (undefined).
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
    updateSheetState(context);
  }

  private void updateSheetState(final FacesContext facesContext) {
    final SheetState state = getSheetState(facesContext);
    if (state != null) {
      final Map attributes = getAttributes();
      //noinspection unchecked
      final List<Integer> list = (List<Integer>) attributes.get(Attributes.SELECTED_LIST_STRING);
      state.setSelectedRows(list != null ? list : Collections.<Integer>emptyList());
      state.setColumnWidths((String) attributes.get(Attributes.WIDTH_LIST_STRING));
      state.setScrollPosition((Integer[]) attributes.get(Attributes.SCROLL_POSITION));
      attributes.remove(Attributes.SELECTED_LIST_STRING);
      attributes.remove(Attributes.SCROLL_POSITION);
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

  public List<AbstractUIColumn> getAllColumns() {
    ArrayList<AbstractUIColumn> result = new ArrayList<AbstractUIColumn>();
    findColumns(this, result, true);
    return result;
  }

  public List<AbstractUIColumn> getRenderedColumns() {
    ArrayList<AbstractUIColumn> result = new ArrayList<AbstractUIColumn>();
    findColumns(this, result, false);
    return result;
  }

  private void findColumns(final UIComponent component, final List<AbstractUIColumn> result, final boolean all) {
    for (final UIComponent child : component.getChildren()) {
      if (all || child.isRendered()) {
        if (child instanceof AbstractUIColumn) {
          result.add((AbstractUIColumn) child);
        } else if (child instanceof AbstractUIData){
          // ignore nested sheets
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
      final UIComponent source = facesEvent.getComponent();
      final UIComponent sourceParent = source.getParent();
      if (sourceParent.getParent() == this
          && source.getId() != null && source.getId().endsWith(SORTER_ID)) {
        facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
        parent.queueEvent(new SortActionEvent(this, (UIColumn) sourceParent));
      } else {
        super.queueEvent(facesEvent);
      }
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
      getSheetState(getFacesContext()).updateSortState((SortActionEvent) facesEvent);
      sort(getFacesContext(), (SortActionEvent) facesEvent);
    }
  }

  public void processEvent(ComponentSystemEvent event) {
    super.processEvent(event);
    if (event instanceof PreRenderComponentEvent) {
      sort(getFacesContext(), null);
      ensureColumnWidthList();
      layoutHeader();
    }
  }

  private void ensureColumnWidthList() {

    final List<AbstractUIColumn> allColumns = getAllColumns();
    final  List<Integer> currentWidthList = new ArrayList<Integer>(allColumns.size() + 1);
    for (int i = 0; i < allColumns.size(); i++) {
      final AbstractUIColumn column = allColumns.get(i);
      currentWidthList.add(null);
    }

    setWidthList(currentWidthList);
  }

  private void layoutHeader() {
    final UIComponent header = getHeader();
    if (header == null) {
      LOG.warn("This should not happen. Please file a bug in the issue tracker to reproduce this case.");
      return;
    }
    final LayoutTokens columns = new LayoutTokens();
    final List<AbstractUIColumn> renderedColumns = getRenderedColumns();
    for (final AbstractUIColumn ignored : renderedColumns) {
      columns.addToken(RelativeLayoutToken.DEFAULT_INSTANCE);
    }
    final LayoutTokens rows = new LayoutTokens();
    rows.addToken(AutoLayoutToken.INSTANCE);
    final Grid grid = new Grid(columns, rows);

    for(final UIComponent child : header.getChildren()) {
        if (child.isRendered()) {
//     XXX not implemented in the moment     grid.add(new OriginCell(child), c.getColumnSpan(), c.getRowSpan());
          grid.add(new OriginCell(child), 1, 1);
        }
    }
    setHeaderGrid(grid);
  }

  protected void sort(FacesContext facesContext, SortActionEvent event) {
    final SheetState sheetState = getSheetState(getFacesContext());
    if (sheetState.isToBeSorted()) {
      final MethodExpression expression = getSortActionListenerExpression();
      if (expression != null) {
        try {
          if (event == null) {
            event =
                new SortActionEvent(this, (UIColumn) findComponent(getSheetState(facesContext).getSortedColumnId()));
          }
          expression.invoke(facesContext.getELContext(), new Object[]{event});
        } catch (Exception e) {
          LOG.warn("Sorting not possible!", e);
        }
      } else {
        new Sorter().perform(this);
      }
      sheetState.setToBeSorted(false);
    }
  }

  public void addStateChangeListener(final SheetStateChangeListener listener) {
    addFacesListener(listener);
  }

  public SheetStateChangeListener[] getStateChangeListeners() {
    return (SheetStateChangeListener[]) getFacesListeners(SheetStateChangeListener.class);
  }

  public void removeStateChangeListener(final SheetStateChangeListener listener) {
    removeFacesListener(listener);
  }

  public List<Integer> getWidthList() {
    return widthList;
  }

  public void setWidthList(final List<Integer> widthList) {
    this.widthList = widthList;
  }



  public Integer[] getScrollPosition() {
    Integer[] scrollPosition = (Integer[]) getAttributes().get(Attributes.SCROLL_POSITION);
    if (scrollPosition == null) {
      scrollPosition = getSheetState(FacesContext.getCurrentInstance()).getScrollPosition();
    }
    return scrollPosition;
  }


  @Override
  public UIComponent findComponent(final String searchId) {
    return super.findComponent(stripRowIndex(searchId));
  }

  public String stripRowIndex(String searchId) {
    if (searchId.length() > 0 && Character.isDigit(searchId.charAt(0))) {
      for (int i = 1; i < searchId.length(); ++i) {
        final char c = searchId.charAt(i);
        if (c == UINamingContainer.getSeparatorChar(getFacesContext())) {
          searchId = searchId.substring(i + 1);
          break;
        }
        if (!Character.isDigit(c)) {
          break;
        }
      }
    }
    return searchId;
  }

  public void performPaging(final PageActionEvent pageEvent) {

    int first;

    if (LOG.isDebugEnabled()) {
      LOG.debug("action = '" + pageEvent.getAction().name() + "'");
    }

    switch (pageEvent.getAction()) {
      case FIRST:
        first = 0;
        break;
      case PREV:
        first = getFirst() - getRows();
        first = first < 0 ? 0 : first;
        break;
      case NEXT:
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
      case LAST:
        first = getFirstRowIndexOfLastPage();
        break;
      case TO_ROW:
        first = pageEvent.getValue() - 1;
        if (hasRowCount() && first > getFirstRowIndexOfLastPage()) {
          first = getFirstRowIndexOfLastPage();
        } else if (first < 0) {
          first = 0;
        }
        break;
      case TO_PAGE:
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

    final ValueExpression expression = getValueExpression(Attributes.FIRST);
    if (expression != null) {
      expression.setValue(getFacesContext().getELContext(), first);
    } else {
      setFirst(first);
    }

    getState().setFirst(first);
  }

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
  }

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
}
