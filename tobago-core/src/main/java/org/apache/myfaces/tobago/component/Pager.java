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
 * Created 04.03.2005 12:28:08.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.event.ActionEvent;

public class Pager extends MethodBinding {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(Pager.class);
  public static final String FIRST = "First";
  public static final String PREV = "Prev";
  public static final String NEXT = "Next";
  public static final String LAST = "Last";
  public static final String PAGE_TO_ROW = "pageToRow";
  public static final String PAGE_TO_PAGE = "pageToPage";
  public static final String LINK_TO_PAGE = "linkToPage";

// ----------------------------------------------------------- business methods

  public Class getType(FacesContext facescontext)
      throws MethodNotFoundException {
    return String.class;
  }

  public Object invoke(FacesContext facesContext, Object aobj[])
      throws EvaluationException, MethodNotFoundException {
    if (aobj[0] instanceof ActionEvent) {
      UICommand command = (UICommand) ((ActionEvent) aobj[0]).getSource();
      UIData data = (UIData) command.getParent();
      String action = (String)
          command.getAttributes().get(ATTR_ACTION_STRING);

      if (LOG.isDebugEnabled()) {
        LOG.debug("action = '" + action + "'");
      }

      if (FIRST.equals(action)) {
        data.setFirst(0);
      } else if (PREV.equals(action)) {
        int start = data.getFirst() - data.getRows();
        data.setFirst(start < 0 ? 0 : start);
      } else if (NEXT.equals(action)) {
        int start = data.getFirst() + data.getRows();
        int last = data.getLastPageIndex();
        data.setFirst(start > data.getRowCount() ? last : start);
      } else if (LAST.equals(action)) {
        data.setFirst(data.getLastPageIndex());
      } else if (PAGE_TO_ROW.equals(action)) {
        String startRow = (String) facesContext.getExternalContext()
            .getRequestParameterMap().get(command.getClientId(
                facesContext) + SUBCOMPONENT_SEP +
            "value");
        if (startRow != null) {
          try {
            int start = Integer.parseInt(startRow) - 1;
            if (start > data.getLastPageIndex()) {
              start = data.getLastPageIndex();
            } else if (start < 0) {
              start = 0;
            }
            data.setFirst(start);
          } catch (NumberFormatException e) {
            LOG.error("Catched: " + e.getMessage());
          }
        } else {
          LOG.error("Can't find 'PageToRow' parameter : " +
              command.getClientId(facesContext) +
              SUBCOMPONENT_SEP +
              "value");
        }
      } else if (PAGE_TO_PAGE.equals(action)) {
        String startRow = (String)
            facesContext.getExternalContext().getRequestParameterMap().get(command.getClientId(
                facesContext) +
            SUBCOMPONENT_SEP +
            "value");
        if (startRow != null) {
          try {
            int start = Integer.parseInt(startRow) - 1;
            if (LOG.isDebugEnabled()) {
              LOG.debug("start = " + start + "  data.getRows() = " +
                  data.getRows() +
                  " => start = " +
                  (start * data.getRows()));
            }
            start = start * data.getRows();
            if (start > data.getLastPageIndex()) {
              start = data.getLastPageIndex();
            } else if (start < 0) {
              start = 0;
            }
            data.setFirst(start);
          } catch (NumberFormatException e) {
            LOG.error("Catched: " + e.getMessage());
          }
        } else {
          LOG.error("Can't find 'PageToRow' parameter : " +
              command.getClientId(facesContext) +
              SUBCOMPONENT_SEP +
              "value");
        }
      } else {
        LOG.error("Unkown action: " + action);
      }

      /*MethodBinding stateChangeListener = data.getStateChangeListener();
      if (stateChangeListener != null) {
        stateChangeListener.invoke(facesContext,
            new Object[] {new SheetStateChangeEvent(data)});
      } */
      data.queueEvent(new SheetStateChangeEvent(data));
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("aobj[0] instanceof '" + aobj[0] + "'");
      }
    }

//    data.updateSheetState(facesContext);

    return null;
  }
}

