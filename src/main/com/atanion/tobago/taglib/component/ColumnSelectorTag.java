/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIColumnSelector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;

public class ColumnSelectorTag extends ColumnTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ColumnSelectorTag.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIColumnSelector.COMPONENT_TYPE;
  }
  
// ///////////////////////////////////////////// bean getter + setter

}
