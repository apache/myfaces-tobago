/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 27.04.2004 at 18:33:04.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.model.SheetState;
import com.atanion.tobago.renderkit.html.scarborough.standard.tag.SheetRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIColumn;

public class UIColumnSelector extends UIColumn{

  private static final Log LOG = LogFactory.getLog(UIColumnSelector.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.ColumnSelector";

}