/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:10:33.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITree;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasIdReference;
import com.atanion.tobago.taglib.decl.HasNameReference;
import com.atanion.tobago.taglib.decl.HasSelectable;
import com.atanion.tobago.taglib.decl.HasState;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsMutable;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.IsShowIcons;
import com.atanion.tobago.taglib.decl.IsShowJunctions;
import com.atanion.tobago.taglib.decl.IsShowRoot;
import com.atanion.tobago.taglib.decl.IsShowRootJunction;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

@Tag(name="tree")
public class TreeTag extends TobagoTag
    implements HasId, HasValue, HasState, IsShowJunctions, IsShowIcons,
    IsShowRoot, IsShowRootJunction, HasSelectable, IsMutable,
               HasIdReference, HasNameReference, IsRendered, HasBinding {

// ----------------------------------------------------------------- attributes

  private String value;
  private String state;

  private String showJunctions;
  private String showIcons;
  private String showRoot;
  private String showRootJunction;

  private String selectable;
  private String mutable;

  private String idReference;
  private String nameReference;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITree.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    
    if (state != null && isValueReference(state)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(
          state, getIterationHelper());
      component.setValueBinding(ATTR_STATE, valueBinding);
    }

    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_JUNCTIONS, showJunctions, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_ICONS, showIcons, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_ROOT, showRoot, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_ROOT_JUNCTION, showRootJunction, getIterationHelper());

    ComponentUtil.setStringProperty(component, ATTR_SELECTABLE, selectable, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_MUTABLE, mutable, getIterationHelper());

    ComponentUtil.setStringProperty(component, ATTR_ID_REFERENCE, idReference, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_NAME_REFERENCE, nameReference, getIterationHelper());
  }

  public void release() {
    super.release();
    value = null;
    state = null;
    showJunctions = null;
    showIcons = null;
    showRoot = null;
    showRootJunction = null;
    selectable = null;
    mutable = null;
    idReference = null;
    nameReference = null;
  }
// ------------------------------------------------------------ getter + setter

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getShowIcons() {
    return showIcons;
  }

  public void setShowIcons(String showIcons) {
    this.showIcons = showIcons;
  }

  public String getShowJunctions() {
    return showJunctions;
  }

  public void setShowJunctions(String showJunctions) {
    this.showJunctions = showJunctions;
  }

  public String getShowRoot() {
    return showRoot;
  }

  public void setShowRoot(String showRoot) {
    this.showRoot = showRoot;
  }

  public String getShowRootJunction() {
    return showRootJunction;
  }

  public void setShowRootJunction(String showRootJunction) {
    this.showRootJunction = showRootJunction;
  }

  public String getIdReference() {
    return idReference;
  }

  public void setIdReference(String idReference) {
    this.idReference = idReference;
  }

  public String getSelectable() {
    return selectable;
  }

  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }

  public String getMutable() {
    return mutable;
  }

  public void setMutable(String mutable) {
    this.mutable = mutable;
  }

  public String getNameReference() {
    return nameReference;
  }

  public void setNameReference(String nameReference) {
    this.nameReference = nameReference;
  }
}

