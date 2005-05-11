/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:10:33.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITreeListbox;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasIdReference;
import com.atanion.tobago.taglib.decl.HasNameReference;
import com.atanion.tobago.taglib.decl.HasState;
import com.atanion.tobago.taglib.decl.HasTreeNodeValue;
import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

/**
 * Renders a listbox view of a tree.
 */
@Tag(name="treeListbox")
@BodyContentDescription(anyTagOf="<f:facet>* <f:actionListener>?" )
public class TreeListboxTag extends TobagoTag
    implements HasIdBindingAndRendered, HasTreeNodeValue, HasState,
               HasIdReference, HasNameReference
    {

  private static final Log LOG = LogFactory.getLog(TreeListboxTag.class);

// ----------------------------------------------------------------- attributes

  private String value;
  private String state;

  private String idReference;
  private String nameReference;

  private String selectable = "single";


// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITreeListbox.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(
        component, ATTR_VALUE, value, getIterationHelper());

    if (state != null && isValueReference(state)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(
          state, getIterationHelper());
      component.setValueBinding(ATTR_STATE, valueBinding);
    }

    ComponentUtil.setStringProperty(
        component, ATTR_ID_REFERENCE, idReference, getIterationHelper());
    ComponentUtil.setStringProperty(
        component, ATTR_NAME_REFERENCE, nameReference, getIterationHelper());

    ComponentUtil.setStringProperty(
        component, ATTR_SELECTABLE, selectable, getIterationHelper());

  }

  public void release() {
    super.release();
    value = null;
    state = null;
    idReference = null;
    nameReference = null;
    selectable = "single";
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

  public String getSelectable() {
    return selectable;
  }


  /**
   * Flag indicating whether or not this component should be render selectable items.
   *  Possible values are:
   *  <ul>
   *  <li><strong>single</strong> : a singlesection tree is rendered
   *  <li><strong>singleLeafOnly</strong> : a singlesection tree is rendered,
   *      only Leaf's are selectable
   *  </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="single")
  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }
}

