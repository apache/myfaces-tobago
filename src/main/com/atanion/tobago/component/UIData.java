/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 27.04.2004 at 18:33:04.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.renderkit.html.scarborough.standard.tag.SheetRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class UIData extends javax.faces.component.UIData {

  private static final Log LOG = LogFactory.getLog(UIData.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Data";


  public void processUpdates(FacesContext context) {
    super.processUpdates(context);

    updateState(context, this);

  }

  public void updateState(FacesContext context, UIData data) {
    ValueBinding stateBinding
        = data.getValueBinding(TobagoConstants.ATTR_STATE_BINDING);
    if (stateBinding != null) {
      SheetRenderer.Sorter sorter =  (SheetRenderer.Sorter)
          getAttributes().get(TobagoConstants.ATTR_SHEET_SORTER);
      SheetRenderer.State state = new SheetRenderer.State();
      state.setFirst(data.getFirst());
      state.setSortedColumn(sorter != null ? sorter.getColumn() : -1);
      state.setAscending(sorter != null && sorter.isAscending());
      stateBinding.setValue(context, state);
    }
    else {
      LOG.info("stateBinding == null");
    }
  }

}