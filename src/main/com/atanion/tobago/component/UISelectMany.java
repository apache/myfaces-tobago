/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 20, 2002 at 11:39:23 AM.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;

public class UISelectMany extends javax.faces.component.UISelectMany {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UISelectMany.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  /** this bugfix allows to select nothing, todo: only for jsfbeta */
  public void validate(FacesContext context) {
    Object value = getValue();
    if (value != null && isValid()) {
      Object values[] = (Object[]) value;
      if (values.length == 0) {
//      Nothing selected: okay! 
        return;
      }
    }
    super.validate(context);
  }

// ///////////////////////////////////////////// bean getter + setter

}
