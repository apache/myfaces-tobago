/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasActionListener;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasIdReference;
import org.apache.myfaces.tobago.taglib.decl.HasNameReference;
import org.apache.myfaces.tobago.taglib.decl.HasState;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTreeNodeValue;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/**
 * Renders a tree view.
 */
@Deprecated
@Tag(name = "tree")
@BodyContentDescription(anyTagOf = "<f:facet>* <f:actionListener>?")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeOld",
    rendererType = "TreeOld")
public interface TreeOldTagDeclaration extends TobagoTagDeclaration,
    HasIdBindingAndRendered, HasTreeNodeValue, HasState, HasIdReference,
    HasActionListener, HasNameReference, IsRequired, HasTabIndex {

  /**
   * Flag indicating whether or not this component should be render selectable items.
   * Possible values are:
   * <ul>
   * <li><strong>multi</strong> : a multisection tree is rendered</li>
   * <li><strong>single</strong> : a singlesection tree is rendered</li>
   * <li><strong>multiLeafOnly</strong> : a multisection tree is rendered,
   * only Leaf's are selectable</li>
   * <li><strong>singleLeafOnly</strong> : a singlesection tree is rendered,
   * only Leaf's are selectable</li>
   * </ul>
   * For any other value or if this attribute is omited the items are not selectable.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "off",
      allowedValues = {"multi", "single", "multiLeafOnly", "singleLeafOnly", "off"})
  void setSelectable(String selectable);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean",
      defaultValue = "false")
  void setMutable(String mutable);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean",
      defaultValue = "false")
  void setShowRootJunction(String showRootJunction);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean",
      defaultValue = "false")
  void setShowIcons(String showIcons);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean",
      defaultValue = "false")
  void setShowJunctions(String showJunctions);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean",
      defaultValue = "false")
  void setShowRoot(String showRoot);

  /**
   * Bean property reference to fetch the disabled state for the treeNode's.<br />
   * Example:<br />
   * a disabledReference="userObject.disabled" try's to invoke
   * <code>&lt;UITreeNode>.getUserObject().getDisabled()</code> to fetch the state.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setDisabledReference(String id);

  /**
   * Display option: Normal tree or menu.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "tree",
      allowedValues = {"tree", "menu"})
  void setMode(String mode);


  /**
   * reference to tip value.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setTipReference(String tipReference);

}
