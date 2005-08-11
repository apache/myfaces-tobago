/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 04.03.2005 12:28:08.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.component.UICommand;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
          command.getAttributes().get(TobagoConstants.ATTR_ACTION_STRING);

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
                facesContext) + TobagoConstants.SUBCOMPONENT_SEP +
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
              TobagoConstants.SUBCOMPONENT_SEP +
              "value");
        }
      } else if (PAGE_TO_PAGE.equals(action)) {
        String startRow = (String)
            facesContext.getExternalContext().getRequestParameterMap().get(command.getClientId(
                facesContext) +
            TobagoConstants.SUBCOMPONENT_SEP +
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
              TobagoConstants.SUBCOMPONENT_SEP +
              "value");
        }
      } else {
        LOG.error("Unkown action: " + action);
      }

      MethodBinding stateChangeListener = data.getStateChangeListener();
      if (stateChangeListener != null) {
        stateChangeListener.invoke(facesContext,
            new Object[] {new SheetStateChangeEvent(data)});
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("aobj[0] instanceof '" + aobj[0] + "'");
      }
    }

//    data.updateSheetState(facesContext);

    return null;
  }
}

