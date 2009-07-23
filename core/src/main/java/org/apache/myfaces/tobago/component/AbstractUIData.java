package org.apache.myfaces.tobago.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeListener;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtil;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractUIData extends javax.faces.component.UIData
    implements SheetStateChangeSource, SortActionSource, AjaxComponent, InvokeOnComponent {

  private static final Log LOG = LogFactory.getLog(AbstractUIData.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  /**
   * @see Facets
   * @deprecated Please use Facets instead. Will be removed after Tobago 1.1 */
  @Deprecated
  public static final String FACET_SORTER = "sorter";
  public static final String SORTER_ID = "sorter";
  /**
   * @see Attributes
   * @deprecated Please use Attributes instead. Will be removed after Tobago 1.1 */
  @Deprecated
  public static final String ATTR_SCROLL_POSITION = "attrScrollPosition";

  public static final String NONE = "none";
  public static final String SINGLE = "single";
  public static final String MULTI = "multi";
  public static final int DEFAULT_DIRECT_LINK_COUNT = 9;
  public static final int DEFAULT_ROW_COUNT = 100;
  public static final String ROW_IDX_REGEX = "^\\d+" + SEPARATOR_CHAR + ".*";

  private SheetState sheetState;
  private List<Integer> widthList;
  private transient LayoutTokens columnLayout;

  public void encodeBegin(FacesContext facesContext) throws IOException {
    SheetState state = getSheetState(facesContext);
    if (state.getFirst() > -1 && state.getFirst() < getRowCount()) {
      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.FIRST)) {
        FacesUtils.setValueOfBindingOrExpression(facesContext, state.getFirst(), this, Attributes.FIRST);
      } else {
        setFirst(state.getFirst());
      }
    }
    super.encodeBegin(facesContext);
  }

  public void setState(SheetState state) {
    this.sheetState = state;
  }

  public SheetState getState() {
    return getSheetState(FacesContext.getCurrentInstance());
  }

  public SheetState getSheetState(FacesContext facesContext) {
    if (sheetState != null) {
      return sheetState;
    } else {
      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.STATE)) {
        SheetState state = (SheetState)
            FacesUtils.getValueFromValueBindingOrValueExpression(facesContext, this, Attributes.STATE);
        if (state == null) {
          state = new SheetState();
          FacesUtils.setValueOfBindingOrExpression(facesContext, state, this, Attributes.STATE);
        }
        return state;
      } else {
        sheetState = new SheetState();
        return sheetState;
      }
    }
  }

  public abstract String getColumns();

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      String columns = getColumns();
      if (columns != null) {
        columnLayout = LayoutTokens.parse(columns);
      }
    }
    return columnLayout;
  }

  public int getLast() {
    int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  public int getPage() {
    int first = getFirst() + 1;
    int rows = getRows();
    if (rows == 0) {
      // avoid division by zero
      return 0;
    }
    if ((first % rows) > 0) {
      return (first / rows) + 1;
    } else {
      return (first / rows);
    }
  }

  public int getPages() {
    int rows = getRows();
    if (rows == 0) {
      return 0;
    }
    return getRowCount() / rows + (getRowCount() % rows == 0 ? 0 : 1);
  }

  public List<UIComponent> getRenderedChildrenOf(UIColumn column) {
    List<UIComponent> children = new ArrayList<UIComponent>();
    for (Object o : column.getChildren()) {
      UIComponent kid = (UIComponent) o;
      if (kid.isRendered()) {
        children.add(kid);
      }
    }
    return children;
  }

  public boolean isAtBeginning() {
    return getFirst() == 0;
  }

  public boolean hasRowCount() {
    return getRowCount() != -1;
  }

  public boolean isAtEnd() {
    if (!hasRowCount()) {
      setRowIndex(getFirst() + getRows() + 1);
      return !isRowAvailable();
    } else {
      return getFirst() >= getLastPageIndex();
    }
  }

  public int getLastPageIndex() {
    int rows = getRows();
    if (rows == 0) {
      // avoid division by zero
      return 0;
    }
    int rowCount = getRowCount();
    int tail = rowCount % rows;
    return rowCount - (tail != 0 ? tail : rows);
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updateSheetState(context);
  }

  private void updateSheetState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    if (state != null) {
      // ensure sortActionListener
//      getSortActionListener();
//      state.setSortedColumn(sortActionListener != null ? sortActionListener.getColumn() : -1);
//      state.setAscending(sortActionListener != null && sortActionListener.isAscending());
      Map attributes = getAttributes();
      //noinspection unchecked
      state.setSelectedRows((List<Integer>) attributes.get(Attributes.SELECTED_LIST_STRING));
      state.setColumnWidths((String) attributes.get(Attributes.WIDTH_LIST_STRING));
      state.setScrollPosition((Integer[]) attributes.get(Attributes.SCROLL_POSITION));
      attributes.remove(Attributes.SELECTED_LIST_STRING);
      attributes.remove(Attributes.SCROLL_POSITION);
    }
  }


  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[2];
    saveState[0] = super.saveState(context);
    saveState[1] = sheetState;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    sheetState = (SheetState) values[1];
  }

  public List<UIColumn> getAllColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && !(kid instanceof ColumnEvent)) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

  public List<UIColumn> getRenderedColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>) getChildren()) {
      if (kid instanceof UIColumn && kid.isRendered() && !(kid instanceof ColumnEvent)) {
        columns.add((UIColumn) kid);
      }
    }
    return columns;
  }

/*  public MethodBinding getSortActionListener() {
    if (sortActionListener != null) {
      return sortActionListener;
    } else {
      return new Sorter();
    }
  }*/

  public void queueEvent(FacesEvent facesEvent) {
    UIComponent parent = getParent();
    if (parent == null) {
      throw new IllegalStateException(
          "component is not a descendant of a UIViewRoot");
    }

    if (facesEvent.getComponent() == this
        && (facesEvent instanceof SheetStateChangeEvent
        || facesEvent instanceof PageActionEvent)) {
      facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      if (LOG.isInfoEnabled()) {
        LOG.info("queueEvent = \"" + facesEvent + "\"");
      }
      parent.queueEvent(facesEvent);
    } else {
      UIComponent source = facesEvent.getComponent();
      UIComponent sourceParent = source.getParent();
      if (sourceParent.getParent() == this
          && source.getId() != null && source.getId().endsWith(SORTER_ID)) {
        facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
        parent.queueEvent(new SortActionEvent(this, (UIColumn) sourceParent));
      } else {
        super.queueEvent(facesEvent);
      }
    }
  }

  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof SheetStateChangeEvent) {
      invokeMethodBinding(getStateChangeListener(), facesEvent);
    } else if (facesEvent instanceof PageActionEvent) {
      if (facesEvent.getComponent() == this) {
        performPaging((PageActionEvent) facesEvent);
        invokeMethodBinding(getStateChangeListener(), new SheetStateChangeEvent(this));
      }
    } else if (facesEvent instanceof SortActionEvent) {
      getSheetState(getFacesContext()).updateSortState((SortActionEvent) facesEvent);
      MethodBinding methodBinding = getSortActionListener();
      if (methodBinding!= null) {
        invokeMethodBinding(methodBinding, facesEvent);
      } else {
        new Sorter().perform((SortActionEvent) facesEvent);
      }
    }
  }

  private void invokeMethodBinding(MethodBinding methodBinding, FacesEvent event) {
    if (methodBinding != null && event != null) {
      try {
        Object[] objects = new Object[]{event};
        methodBinding.invoke(getFacesContext(), objects);
      } catch (EvaluationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof AbortProcessingException) {
          throw (AbortProcessingException) cause;
        } else {
          throw e;
        }
      }
    }
  }

  public void addStateChangeListener(SheetStateChangeListener listener) {
    addFacesListener(listener);
  }

  public SheetStateChangeListener[] getStateChangeListeners() {
    return (SheetStateChangeListener[]) getFacesListeners(SheetStateChangeListener.class);
  }

  public void removeStateChangeListener(SheetStateChangeListener listener) {
    removeFacesListener(listener);
  }

  public List<Integer> getWidthList() {
    return widthList;
  }

  public void setWidthList(List<Integer> widthList) {
    this.widthList = widthList;
  }

  public void processDecodes(FacesContext context) {
    if (context instanceof TobagoFacesContext && ((TobagoFacesContext) context).isAjax()) {
      final String ajaxId = ((TobagoFacesContext) context).getAjaxComponentId();
      UIComponent reload = getFacet(Facets.RELOAD);
      if (ajaxId != null && ajaxId.equals(getClientId(context)) && reload != null && reload.isRendered()
          && ajaxId.equals(ComponentUtil.findPage(context, this).getActionId())) {
        Boolean immediate = (Boolean) reload.getAttributes().get(Attributes.IMMEDIATE);
        if (immediate != null && immediate) {
          Boolean update = (Boolean) reload.getAttributes().get(Attributes.UPDATE);
          if (update != null && !update) {
            if (context.getExternalContext().getResponse() instanceof HttpServletResponse) {
              ((HttpServletResponse) context.getExternalContext().getResponse())
                  .setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
            context.responseComplete();
            return;
          }
        }
      }
    }
    super.processDecodes(context);
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    // TODO neets more testing!!!
    //if (!facesContext.getRenderResponse() && !ComponentUtil.hasErrorMessages(facesContext)) {
    // in encodeBegin of superclass is some logic which clears the DataModel
    // this must here also done.
    // in RI and myfaces this could done via setValue(null)

    if (FacesUtils.hasValueBindingOrValueExpression(this, "value")) {
      setValue(null);
    } else {
      setValue(getValue());
    }
    //}
    UIComponent reload = getFacet(Facets.RELOAD);
    if (reload != null && reload.isRendered()) {
      Boolean immediate = (Boolean) reload.getAttributes().get(Attributes.IMMEDIATE);
      if (immediate != null && !immediate) {
        Boolean update = (Boolean) reload.getAttributes().get(Attributes.UPDATE);
        if (update != null && !update) {
          return;
        }
      }
    }
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public Integer[] getScrollPosition() {
    Integer[] scrollPosition = (Integer[]) getAttributes().get(Attributes.SCROLL_POSITION);
    if (scrollPosition == null) {
      scrollPosition = getSheetState(FacesContext.getCurrentInstance()).getScrollPosition();
    }
    return scrollPosition;
  }


  public UIComponent findComponent(String searchId) {
    if (searchId.matches(ROW_IDX_REGEX)) {
      searchId = searchId.substring(searchId.indexOf(SEPARATOR_CHAR) + 1);
    }
    return super.findComponent(searchId);
  }

  public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback)
      throws FacesException {
    // we may need setRowIndex on UIData
    int oldRowIndex = getRowIndex();
    try {
      String sheetId = getClientId(context);
      String idRemainder = clientId.substring(sheetId.length());
      if (LOG.isInfoEnabled()) {
        LOG.info("idRemainder = \"" + idRemainder + "\"");
      }
      if (idRemainder.matches("^:\\d+:.*")) {
        idRemainder = idRemainder.substring(1);
        int idx = idRemainder.indexOf(":");
        try {
          int rowIndex = Integer.parseInt(idRemainder.substring(0, idx));
          if (LOG.isInfoEnabled()) {
            LOG.info("set rowIndex = \"" + rowIndex + "\"");
          }
          setRowIndex(rowIndex);

        } catch (NumberFormatException e) {
          if (LOG.isInfoEnabled()) {
            LOG.error("idRemainder = \"" + idRemainder + "\"", e);
          }
        }
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info("no match for \"^:\\d+:.*\"");
        }
      }

      return FacesUtils.invokeOnComponent(context, this, clientId, callback);

    } finally {
      // we should reset rowIndex on UIData
      setRowIndex(oldRowIndex);
    }
  }

  public void performPaging(PageActionEvent pageEvent) {

    int first = -1;

    if (LOG.isDebugEnabled()) {
      LOG.debug("action = '" + pageEvent.getAction().name() + "'");
    }

    int start;
    switch (pageEvent.getAction()) {
      case FIRST:
        first = 0;
        break;
      case PREV:
        start = getFirst() - getRows();
        first = start < 0 ? 0 : start;
        break;
      case NEXT:
        if (hasRowCount()) {
          start = getFirst() + getRows();
          first = start > getRowCount() ? getLastPageIndex() : start;
        } else {
          if (isAtEnd()) {
            first = getFirst();
          } else {
            first = getFirst() + getRows();
          }
        }
        break;
      case LAST:
        first = getLastPageIndex();
        break;
      case TO_ROW:
        start = pageEvent.getValue() - 1;
        if (start > getLastPageIndex()) {
          start = getLastPageIndex();
        } else if (start < 0) {
          start = 0;
        }
        first = start;
        break;
      case TO_PAGE:
        start = pageEvent.getValue() - 1;
        if (LOG.isDebugEnabled()) {
          LOG.debug("start = " + start + "  sheet.getRows() = "
              + getRows() + " => start = " + (start * getRows()));
        }
        start = start * getRows();
        if (start > getLastPageIndex()) {
          start = getLastPageIndex();
        } else if (start < 0) {
          start = 0;
        }
        first = start;
        break;
      default:
        // can't happen
    }

    if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.FIRST)) {
      FacesUtils.setValueOfBindingOrExpression(FacesContext.getCurrentInstance(), first, this, Attributes.FIRST);
    } else {
      setFirst(first);
    }

    getState().setFirst(first);
//      sheet.queueEvent(new SheetStateChangeEvent(sheet));
  }

}
