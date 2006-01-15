/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Created 27.04.2004 at 18:33:04.
  * $Id$
  */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.SheetRendererWorkaround;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.util.StringUtil;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SheetStateChangeListener;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.el.EvaluationException;
import javax.faces.event.FacesEvent;
import javax.faces.event.AbortProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UIData extends javax.faces.component.UIData
    implements SheetStateChangeSource, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UIData.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Data";

  private MethodBinding stateChangeListener;

  private List<Integer> widthList;

  // TODO: should be removed?
  private Sorter sorter;

  private SheetState state;

  private boolean showHeader = true;
  private boolean showHeaderSet = false;


  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.prepareDimension(facesContext, this);
    super.encodeBegin(facesContext);
  }

  public void encodeEnd(FacesContext facesContext) throws IOException {
    setupState(facesContext);
    prepareDimensions(facesContext);
    super.encodeEnd(facesContext);
  }

  private void setupState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    ensureColumnWidthList(facesContext, state);
  }

  public void setState(SheetState state) {
    this.state = state;
  }

  public SheetState getSheetState(FacesContext facesContext) {
    if (state != null) {
      return state;
    } else {
      ValueBinding stateBinding = getValueBinding(ATTR_STATE);
      if (stateBinding != null) {
        SheetState state = (SheetState) stateBinding.getValue(facesContext);
        if (state == null) {
          state = new SheetState();
          stateBinding.setValue(facesContext, state);
        }
        return state;
      } else {
        state = new SheetState();
        return state;
      }
    }
  }

  private void ensureColumnWidthList(FacesContext facesContext, SheetState state) {
    List<Integer> widthList = null;
    List<UIColumn> columns = getRendererdColumns();

    final Map attributes = getAttributes();
    String widthListString = null;

    if (state != null) {
      widthListString = state.getColumnWidths();
    }
    if (widthListString == null) {
      widthListString =
          (String) attributes.get(ATTR_WIDTH_LIST_STRING);
    }

    if (widthListString != null) {
      widthList = StringUtil.parseIntegerList(widthListString);
    }
    if (widthList != null && widthList.size() != columns.size()) {
      widthList = null;
    }


    if (widthList == null) {
      String columnLayout =
          (String) attributes.get(ATTR_COLUMNS);
      List<UIColumn> allColumns = getAllColumns();

      if (columnLayout == null && allColumns.size() > 0) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < allColumns.size(); i++) {
          sb.append("1*;");
        }
        columnLayout = sb.deleteCharAt(sb.lastIndexOf(";")).toString();
        if (LOG.isWarnEnabled()) {
          LOG.warn(
              "No columns found! Using created layout tokens: " + columnLayout);
        }
      }
      String[] layoutTokens
          = LayoutInfo.createLayoutTokens(columnLayout, allColumns.size(), "1*");

      // here we have layoutTokens for all columns
      // now remove tokens for unrendered columns
      boolean changed = false;
      for (int i = 0; i < layoutTokens.length; i++) {
        if (!allColumns.get(i).isRendered()) {
          layoutTokens[i] = null;
          changed = true;
        }
      }
      if (changed) {
        String[] allTokens = layoutTokens;
        layoutTokens = new String[columns.size()];
        int j = 0;
        for (int i = 0; i < allTokens.length; i++) {
          if (allTokens[i] != null) {
            layoutTokens[j] = allTokens[i];
            j++;
          }
        }
      }



      int space = LayoutUtil.getInnerSpace(facesContext, this, true);
      SheetRendererWorkaround renderer
          = (SheetRendererWorkaround) ComponentUtil.getRenderer(facesContext, this);
      space -= renderer.getContentBorder(facesContext, this);
      if (renderer.needVerticalScrollbar(facesContext, this)) {
        space -= renderer.getScrollbarWidth(facesContext, this);
      }
      LayoutInfo layoutInfo = new LayoutInfo(getRendererdColumns().size(),
          space, layoutTokens, false);
      parseFixedWidth(facesContext, layoutInfo);
      layoutInfo.parseColumnLayout(space);
      widthList = layoutInfo.getSpaceList();

    }

    if (widthList != null) {
      if (columns.size() != widthList.size()) {
        LOG.warn("widthList.size() = " + widthList.size() +
            " != columns.size() = " + columns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(widthList));
      } else {
        this.widthList = widthList;
      }
    }
  }

  private void parseFixedWidth(FacesContext facesContext, LayoutInfo layoutInfo)
  {
    String[] tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].equals("fixed")) {
        int width = 0;
        final List<UIColumn> columns = getRendererdColumns();
        if (! columns.isEmpty()) {
          if (i < columns.size()) {
            UIColumn column = columns.get(i);
            if (column instanceof UIColumnSelector) {
                RendererBase renderer
                    = ComponentUtil.getRenderer(facesContext, column);
              if (renderer == null) {
                LOG.warn("can't find renderer for " + column.getClass().getName());
                renderer = ComponentUtil.getRenderer(facesContext, UIPanel.COMPONENT_FAMILY, RENDERER_TYPE_OUT);
              }
              width = renderer.getFixedWidth(facesContext, column);

            } else {
              for (UIComponent component : (List<UIComponent>)column.getChildren()) {
                RendererBase renderer
                    = ComponentUtil.getRenderer(facesContext, component);
                width += renderer.getFixedWidth(facesContext, component);
              }
            }
            layoutInfo.update(width, i);
          }
          else {
            layoutInfo.update(0, i);
            if (LOG.isWarnEnabled()) {
              LOG.warn("More LayoutTokens found than rows! skipping!");
            }
          }
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("set column " + i + " from fixed to with " + width);
        }
      }
    }
  }


  private void prepareDimensions(FacesContext facesContext) {
    // prepare width's in column's children components

    List columnWidths = getWidthList();
    int i = 0;
    for (UIColumn column : getRendererdColumns()) {
      if (i < columnWidths.size()) {
        Integer width = (Integer) columnWidths.get(i);
        if (!(column instanceof UIColumnSelector)) {
          if (column.getChildCount() == 1) {
            UIComponent child = (UIComponent) column.getChildren().get(0);
            int cellPaddingWidth = ((RendererBase) getRenderer(facesContext))
                .getConfiguredValue(facesContext, this, "cellPaddingWidth");
            child.getAttributes().put(ATTR_LAYOUT_WIDTH,
                new Integer(width.intValue() - cellPaddingWidth));
          } else {
            LOG.warn("More or less than 1 child in column! "
                + "Can't set width for column " + i + " to " + width);
          }
        }
      }
      else {
        LOG.warn("More columns than columnSizes! "
            + "Can't set width for column " + i);
      }
      i++;
    }
  }

  public int getLast() {
    int last = getFirst() + getRows();
    return last < getRowCount() ? last : getRowCount();
  }

  public int getPage() {
    int first = getFirst() + 1;
    int rows = getRows();  
    if ((first % rows) > 0) {
      return (first / rows) + 1;
    } else {
      return (first / rows) ;
    }
  }

  public int getPages() {
    return getRowCount() / getRows() + (getRowCount() % getRows() == 0 ? 0 : 1);
  }

  public List<UIComponent> getRenderedChildrenOf(UIColumn column) {
    List<UIComponent> children = new ArrayList<UIComponent>();
    for (Iterator kids = column.getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (kid.isRendered()) {
        children.add(kid);
      }
    }
    return children;
  }

  public boolean isAtBeginning() {
    return getFirst() == 0;
  }

  public boolean isAtEnd() {
    return getFirst() >= getLastPageIndex();
  }

  public int getLastPageIndex() {
    int tail = getRowCount() % getRows();
    return getRowCount() - (tail != 0 ? tail : getRows());
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updateSheetState(context);
  }

  private void updateSheetState(FacesContext facesContext) {
    SheetState state = getSheetState(facesContext);
    if (state != null) {
      // ensure sorter
//      getSorter();
//      state.setSortedColumn(sorter != null ? sorter.getColumn() : -1);
//      state.setAscending(sorter != null && sorter.isAscending());
      state.setSelectedRows((List<Integer>)
          getAttributes().get(ATTR_SELECTED_LIST_STRING));
      state.setColumnWidths((String)
          getAttributes().get(ATTR_WIDTH_LIST_STRING));
    }
  }

  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[5];
    saveState[0] = super.saveState(context);
    saveState[1] = state;
    saveState[2] = sorter;
    saveState[3] = stateChangeListener;
    if (showHeaderSet) {
      saveState[4] = showHeader;
    }
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    state = (SheetState) values[1];
    sorter = (Sorter) values[2];
    stateChangeListener = (MethodBinding) values[3];
    if (values[4] != null) {
      showHeaderSet = true;
      showHeader = (Boolean) values[4];
    }
  }

// ------------------------------------------------------------ getter + setter

  public List<UIColumn> getAllColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>)getChildren()) {
      if (kid instanceof UIColumn) {
        columns.add((UIColumn)kid);
      }
    }
    return columns;
  }

  public List<UIColumn> getRendererdColumns() {
    List<UIColumn> columns = new ArrayList<UIColumn>();
    for (UIComponent kid : (List<UIComponent>)getChildren()) {
      if (kid instanceof UIColumn && kid.isRendered()) {
        columns.add((UIColumn)kid);
      }
    }
    return columns;
  }

  // TODO: should be removed?
  public Sorter getSorter() {
    if (sorter != null) {
      return sorter;
    } else {
      return new Sorter();
    }
  }

  public void setSorter(Sorter sorter) {
    this.sorter = sorter;
  }

  public void queueEvent(FacesEvent facesEvent) {
    if (facesEvent instanceof SheetStateChangeEvent) {
      UIComponent parent = getParent();
      if (parent == null) {
        throw new IllegalStateException("component is not a descendant of a UIViewRoot");
      }
      parent.queueEvent(facesEvent);
    } else {
      super.queueEvent(facesEvent);
    }

  }

  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if ( facesEvent instanceof SheetStateChangeEvent) {
      MethodBinding sheetChangeListenerBinding = getStateChangeListener();
      if (sheetChangeListenerBinding != null) {
        try {
          sheetChangeListenerBinding.invoke(getFacesContext(), new Object[]{(SheetStateChangeEvent)facesEvent});
        } catch (EvaluationException e) {
          Throwable cause = e.getCause();
          if (cause != null && cause instanceof AbortProcessingException) {
            throw (AbortProcessingException)cause;
          } else {
            throw e;
          }
        }
      }
    }
  }

  public void addStateChangeListener(SheetStateChangeListener listener) {
    addFacesListener(listener);
  }

  public SheetStateChangeListener[] getStateChangeListeners() {
    return  (SheetStateChangeListener[])getFacesListeners(SheetStateChangeListener.class);
  }

  public void removeStateChangeListener(SheetStateChangeListener listener) {
    removeFacesListener(listener);
  }

  public MethodBinding getStateChangeListener() {
    return stateChangeListener;
  }

  public void setStateChangeListener(MethodBinding stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }

  public List<Integer> getWidthList() {
    return widthList;
  }

  public boolean isShowHeader() {
    if (showHeaderSet) {
        return showHeader;
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_HEADER);
    if (vb != null) {
        return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
        return showHeader;
    }
  }

  public void setShowHeader(boolean showHeader) {
    this.showHeader = showHeader;
    this.showHeaderSet = true;
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {    
    setupState(facesContext);
    prepareDimensions(facesContext);
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public void processAjax(FacesContext facesContext) throws IOException {
    final String ajaxId = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    LOG.info("ajaxId= " + ajaxId + "  :: clientId = " + getClientId(facesContext));
    if (ajaxId.equals(getClientId(facesContext))) {
      AjaxUtils.processActiveAjaxComponent(facesContext, this);
    } else {
      AjaxUtils.processAjaxOnChildren(facesContext, this);
    }
  }
}
