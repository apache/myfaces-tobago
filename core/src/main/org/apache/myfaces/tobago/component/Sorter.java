/*
 * Copyright 2002-2005 atanion GmbH.
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
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.model.SortableByApplication;
import org.apache.myfaces.tobago.util.BeanComparator;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Mar 7, 2005
 * Time: 4:01:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sorter extends MethodBinding {

  private static final Log LOG = LogFactory.getLog(Sorter.class);

  public static final String ID_PREFIX = "sorter_";

//  private UIData data;

//  private int column;
//  private boolean ascending;

//  public Sorter(UIData data) {
//    this.data = data;
//    column = -1;
//    ascending = true;
//  }

//  public Sorter() {
//  }

  public Object invoke(FacesContext facesContext, Object aobj[])
      throws EvaluationException, MethodNotFoundException {
    if (aobj[0] instanceof ActionEvent) {
      javax.faces.component.UICommand command =
          (javax.faces.component.UICommand) ((ActionEvent) aobj[0]).getSource();
      if (LOG.isDebugEnabled()) {
        LOG.debug("sorterId = " + command.getId());
      }
      UIData data = (UIData) command.getParent();
      Object value = data.getValue();
      if (value instanceof DataModel) {
        value = ((DataModel) value).getWrappedData();
      }
      SheetState sheetState = data.getSheetState(facesContext);
      int column = sheetState.getSortedColumn();
      boolean ascending = sheetState.isAscending();


      if (value instanceof SortableByApplication
        || value instanceof List
        || value instanceof Object[]) {
        String sortProperty;

        if (command.getId() != null && command.getId().startsWith(ID_PREFIX)) {
          UIColumn uiColumn = null;
          try {
            int actualColumn =
                Integer.parseInt(
                    command.getId().substring(ID_PREFIX.length()));
            if (actualColumn == column) {
              ascending = !ascending;
            } else {
              ascending = true;
              column = actualColumn;
            }
            sheetState.setAscending(ascending);
            sheetState.setSortedColumn(column);

            uiColumn = data.getColumns().get(column);
            UIComponent child = getFirstSortableChild(uiColumn.getChildren());
            if (child != null) {
              ValueBinding valueBinding = child.getValueBinding("value");
              String expressionString = valueBinding.getExpressionString();
              if (expressionString.startsWith("#{") &&
                    expressionString.endsWith("}")) {
                expressionString =
                    expressionString.substring(2,
                        expressionString.length() - 1);
              }
              String var = data.getVar();
              sortProperty = expressionString.substring(var.length() + 1);
              if (LOG.isDebugEnabled()) {
                LOG.debug("Sort property is " + sortProperty);
              }
            } else {
              LOG.error("No sortable component found!");
              removeSortableAttribute(uiColumn);
              return null;
            }
          } catch (Exception e) {
            LOG.error("Error while extracting sortMethod :" + e.getMessage(),
                e);
            if (uiColumn != null) {
              removeSortableAttribute(uiColumn);
            }
            return null;
          }
        } else {
          LOG.error(
              "Sorter.invoke() with illegal id in ActionEvent's source");
          return null;
        }

        if (value instanceof SortableByApplication) {
          ((SortableByApplication) value).sortBy(sortProperty);
        } else {
          // todo: locale / comparator parameter?
          // don't compare numbers with Collator.getInstance() comparator
//        Comparator comparator = Collator.getInstance();
          Comparator comparator = null;
          Comparator beanComparator
              = new BeanComparator(sortProperty, comparator, !ascending);
//          comparator = new RowComparator(ascending, method);

          if (value instanceof List) {
            Collections.sort((List) value, beanComparator);
          } else { // if (value instanceof Object[]) {
            Arrays.sort((Object[]) value, beanComparator);
          }
        }
      } else {  // DataModel?, ResultSet, Result or Object
        LOG.warn("Sorting not supported for type "
                   + (value != null ? value.getClass().toString() : "null"));
      }
    }
    return null;
  }

  private void removeSortableAttribute(UIColumn uiColumn) {
    LOG.warn("removing attribute sortable from column " + uiColumn.getId());
    uiColumn.getAttributes().remove(TobagoConstants.ATTR_SORTABLE);
  }

  private UIComponent getFirstSortableChild(List children) {
    UIComponent child = null;

    for (Iterator iter = children.iterator(); iter.hasNext();) {
      child = (UIComponent) iter.next();
      if (child instanceof UICommand
        || child instanceof javax.faces.component.UIPanel) {
        child = getFirstSortableChild(child.getChildren());
      }
      if (child instanceof UISelectMany
        || child instanceof UISelectOne
        || child instanceof UISelectBoolean) {
        continue;
      } else if (child instanceof UIInput &&
        TobagoConstants.RENDERER_TYPE_HIDDEN.equals(child.getRendererType())) {
        continue;
      } else if (child instanceof UIOutput) {
        break;
      }
    }
    return child;
  }

  public Class getType(FacesContext facescontext)
      throws MethodNotFoundException {
    return String.class;
  }

}

