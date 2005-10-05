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
 * Created 28.01.2003 14:10:33.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasIdReference;
import org.apache.myfaces.tobago.taglib.decl.HasNameReference;
import org.apache.myfaces.tobago.taglib.decl.HasState;
import org.apache.myfaces.tobago.taglib.decl.HasTreeNodeValue;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

import javax.faces.component.UIComponent;

/**
 * Renders a listbox view of a tree.
 */
@Tag(name = "treeListbox")
@BodyContentDescription(anyTagOf = "<f:facet>* <f:actionListener>?")
public class TreeListboxTag extends TobagoTag
    implements HasIdBindingAndRendered, HasTreeNodeValue, HasState,
    HasIdReference, HasNameReference, IsRequired {

  private String value;
  private String state;

  private String idReference;
  private String nameReference;

  private String selectable = "single";

  private String required;

  public String getComponentType() {
    return UITreeListbox.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);

    ComponentUtil.setStringProperty(component, ATTR_ID_REFERENCE, idReference);
    ComponentUtil.setStringProperty(component, ATTR_NAME_REFERENCE, nameReference);
    ComponentUtil.setStringProperty(component, ATTR_SELECTABLE, selectable);
    ComponentUtil.setBooleanProperty(component, ATTR_REQUIRED, required);

  }

  public void release() {
    super.release();
    value = null;
    state = null;
    idReference = null;
    nameReference = null;
    selectable = "single";
    required = null;
  }

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
   * Possible values are:
   * <ul>
   * <li><strong>single</strong> : a singleselection tree is rendered
   * <li><strong>singleLeafOnly</strong> : a singleselection tree is rendered,
   * only Leaf's are selectable
   * <li><strong>siblingLeafOnly</strong> : a multiselection tree is rendered,
   * where only sibling Leaf's are selectable
   * </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "single")
  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }
}

