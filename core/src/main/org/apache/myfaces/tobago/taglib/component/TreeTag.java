/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved. Created 28.01.2003 14:10:33.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasIdReference;
import org.apache.myfaces.tobago.taglib.decl.HasNameReference;
import org.apache.myfaces.tobago.taglib.decl.HasState;
import org.apache.myfaces.tobago.taglib.decl.HasTreeNodeValue;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

/**
 * Renders a tree view.
 */
@Tag(name="tree")
@BodyContentDescription(anyTagOf="<f:facet>* <f:actionListener>?" )
public class TreeTag extends TobagoTag
    implements HasIdBindingAndRendered, HasTreeNodeValue, HasState,
               HasIdReference, HasNameReference, IsRequired {

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


  private String required;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITree.COMPONENT_TYPE;
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

    ComponentUtil.setBooleanProperty(
        component, ATTR_SHOW_JUNCTIONS, showJunctions, getIterationHelper());
    ComponentUtil.setBooleanProperty(
        component, ATTR_SHOW_ICONS, showIcons, getIterationHelper());
    ComponentUtil.setBooleanProperty(
        component, ATTR_SHOW_ROOT, showRoot, getIterationHelper());
    ComponentUtil.setBooleanProperty(component,
        ATTR_SHOW_ROOT_JUNCTION, showRootJunction, getIterationHelper());

    ComponentUtil.setStringProperty(
        component, ATTR_SELECTABLE, selectable, getIterationHelper());
    ComponentUtil.setBooleanProperty(
        component, ATTR_MUTABLE, mutable, getIterationHelper());

    ComponentUtil.setStringProperty(
        component, ATTR_ID_REFERENCE, idReference, getIterationHelper());
    ComponentUtil.setStringProperty(
        component, ATTR_NAME_REFERENCE, nameReference, getIterationHelper());


    ComponentUtil.setBooleanProperty(
        component, ATTR_REQUIRED, required, getIterationHelper());
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
    required = null;
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


  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
  public void setShowIcons(String showIcons) {
    this.showIcons = showIcons;
  }

  public String getShowJunctions() {
    return showJunctions;
  }


  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
  public void setShowJunctions(String showJunctions) {
    this.showJunctions = showJunctions;
  }

  public String getShowRoot() {
    return showRoot;
  }

  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
  public void setShowRoot(String showRoot) {
    this.showRoot = showRoot;
  }

  public String getShowRootJunction() {
    return showRootJunction;
  }

  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
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

  /**
   * Flag indicating whether or not this component should be render selectable items.
   *  Possible values are:
   *  <ul>
   *  <li><strong>multi</strong> : a multisection tree is rendered
   *  <li><strong>single</strong> : a singlesection tree is rendered
   *  <li><strong>multiLeafOnly</strong> : a multisection tree is rendered,
   *      only Leaf's are selectable
   *  <li><strong>singleLeafOnly</strong> : a singlesection tree is rendered,
   *      only Leaf's are selectable
   *  </ul>
   *  For any other value or if this attribute is omited the items are not selectable.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="off")
  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }

  public String getMutable() {
    return mutable;
  }

  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
  public void setMutable(String mutable) {
    this.mutable = mutable;
  }

  public String getNameReference() {
    return nameReference;
  }

  public void setNameReference(String nameReference) {
    this.nameReference = nameReference;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }
}

