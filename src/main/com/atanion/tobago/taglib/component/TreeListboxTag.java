/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:10:33.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITree;

import javax.faces.component.UIComponent;

public class TreeListboxTag extends BeanTag {

// ----------------------------------------------------------------- attributes

  private String idReference;
  private String nameReference;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITree.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);


   ComponentUtil.setStringProperty(component, ATTR_ID_REFERENCE, idReference, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_NAME_REFERENCE, nameReference, getIterationHelper());
        
   ComponentUtil.setStringProperty(component, ATTR_SELECTABLE, "single", getIterationHelper());

  }

  public void release() {
    super.release();
    idReference = null;
    nameReference = null;
  }
// ------------------------------------------------------------ getter + setter

  public String getIdReference() {
    return idReference;
  }

  public void setIdReference(String idReference) {
    this.idReference = idReference;
  }

  public String getNameReference() {
    return nameReference;
  }

  public void setNameReference(String nameReference) {
    this.nameReference = nameReference;
  }
}

