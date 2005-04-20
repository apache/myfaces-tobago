/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:10:33.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITreeListbox;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasIdReference;
import com.atanion.tobago.taglib.decl.HasNameReference;
import com.atanion.tobago.taglib.decl.HasState;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

@Tag(name="treeListbox")
public class TreeListboxTag extends TobagoTag
    implements HasId, HasValue, HasState, HasIdReference, HasNameReference,
               IsRendered, HasBinding
    {

// ----------------------------------------------------------------- attributes

  private String value;
  private String state;

  private String idReference;
  private String nameReference;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITreeListbox.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());

    if (state != null && isValueReference(state)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(
          state, getIterationHelper());
      component.setValueBinding(ATTR_STATE, valueBinding);
    }

   ComponentUtil.setStringProperty(component, ATTR_ID_REFERENCE, idReference, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_NAME_REFERENCE, nameReference, getIterationHelper());
        
   ComponentUtil.setStringProperty(component, ATTR_SELECTABLE, "single", getIterationHelper());

  }

  public void release() {
    super.release();
    value = null;
    state = null;
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

