/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:10:33.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UITree;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

public class TreeTag extends BeanTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String hideJunctions;
  private String hideIcons;
  private String hideRoot;
  private String hideRootJunction;

  private String multiselect;
  private String mutable;

  private String idReference;

  private String nameReference;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UITree.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    setProperty(component, TobagoConstants.ATTR_HIDE_JUNCTIONS,
        TobagoConstants.VB_HIDE_JUNCTIONS, hideJunctions);
    setProperty(component, TobagoConstants.ATTR_HIDE_ICONS,
        TobagoConstants.VB_HIDE_ICONS, hideIcons);
    setProperty(component, TobagoConstants.ATTR_HIDE_ROOT,
        TobagoConstants.VB_HIDE_ROOT, hideRoot);
    setProperty(component, TobagoConstants.ATTR_HIDE_ROOT_JUNCTION,
        TobagoConstants.VB_HIDE_ROOT_JUNCTION, hideRootJunction);

    setProperty(component, TobagoConstants.ATTR_MULTISELECT,
        TobagoConstants.VB_MULTISELECT, multiselect);
    setProperty(component, TobagoConstants.ATTR_MUTABLE,
        TobagoConstants.VB_MUTABLE, mutable);

    setProperty(component, TobagoConstants.ATTR_ID_REFERENCE, idReference);
    setProperty(component, TobagoConstants.ATTR_NAME_REFERENCE, nameReference);
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setHideJunctions(String hideJunctions) {
    this.hideJunctions = hideJunctions;
  }

  public void setHideIcons(String hideIcons) {
    this.hideIcons = hideIcons;
  }

  public void setHideRoot(String hideRoot) {
    this.hideRoot = hideRoot;
  }

  public void setHideRootJunction(String hideRootJunction) {
    this.hideRootJunction = hideRootJunction;
  }

  public void setMultiselect(String multiselect) {
    this.multiselect = multiselect;
  }

  public void setMutable(String mutable) {
    this.mutable = mutable;
  }

  public void setIdReference(String idReference) {
    this.idReference = idReference;
  }

  public void setNameReference(String nameReference) {
    this.nameReference = nameReference;
  }
}


