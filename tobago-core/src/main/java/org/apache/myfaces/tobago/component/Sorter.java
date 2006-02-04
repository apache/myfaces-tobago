package org.apache.myfaces.tobago.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SORTABLE;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.model.SortableByApplication;
import org.apache.myfaces.tobago.util.BeanComparator;
import org.apache.myfaces.tobago.util.ValueBindingComparator;

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
 * User: weber
 * Date: Mar 7, 2005
 * Time: 4:01:27 PM
 */
public class Sorter extends MethodBinding {

  private static final Log LOG = LogFactory.getLog(Sorter.class);

  public static final String ID_PREFIX = "sorter_";


  public Object invoke(FacesContext facesContext, Object[] aobj)
      throws EvaluationException {
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

      Comparator comparator = null;

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

            uiColumn = data.getRendererdColumns().get(column);
            UIComponent child = getFirstSortableChild(uiColumn.getChildren());
            if (child != null) {
              ValueBinding valueBinding = child.getValueBinding("value");
              String var = data.getVar();

              if (valueBinding != null) {
                if (isSimpleProperty(valueBinding.getExpressionString())) {
                  String expressionString = valueBinding.getExpressionString();
                  if (expressionString.startsWith("#{")
                      && expressionString.endsWith("}")) {
                    expressionString =
                        expressionString.substring(2,
                            expressionString.length() - 1);
                  }
                  sortProperty = expressionString.substring(var.length() + 1);

                  comparator = new BeanComparator(sortProperty, null, !ascending);

                  if (LOG.isDebugEnabled()) {
                    LOG.debug("Sort property is " + sortProperty);
                  }
                } else {
                  comparator = new ValueBindingComparator(facesContext, var, valueBinding, !ascending);
                }
              }

            } else {
              LOG.error("No sortable component found!");
              removeSortableAttribute(uiColumn);
              return null;
            }
          } catch (Exception e) {
            LOG.error("Error while extracting sortMethod :" + e.getMessage(), e);
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

        //if (value instanceof SortableByApplication) {
            //((SortableByApplication) value).sortBy(sortProperty);

          // TODO ???? sortable by application
        if (!(value instanceof SortableByApplication)) {
          // TODO: locale / comparator parameter?
          // don't compare numbers with Collator.getInstance() comparator
//        Comparator comparator = Collator.getInstance();
//          comparator = new RowComparator(ascending, method);

          if (value instanceof List) {
            Collections.sort((List) value, comparator);
          } else { // if (value instanceof Object[]) {
            Arrays.sort((Object[]) value, comparator);
          }
        }
      } else {  // DataModel?, ResultSet, Result or Object
        LOG.warn("Sorting not supported for type "
                   + (value != null ? value.getClass().toString() : "null"));
      }
    }
    return null;
  }

  private boolean isSimpleProperty(String expressionString) {
    return expressionString.matches("^#\\{(\\w+(\\.\\w)*)\\}$");
  }

  private void removeSortableAttribute(UIColumn uiColumn) {
    LOG.warn("removing attribute sortable from column " + uiColumn.getId());
    uiColumn.getAttributes().remove(ATTR_SORTABLE);
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
      } else if (child instanceof UIInput
          && TobagoConstants.RENDERER_TYPE_HIDDEN.equals(child.getRendererType())) {
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

