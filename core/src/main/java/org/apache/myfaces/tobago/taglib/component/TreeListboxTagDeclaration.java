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
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasIdReference;
import org.apache.myfaces.tobago.taglib.decl.HasNameReference;
import org.apache.myfaces.tobago.taglib.decl.HasState;
import org.apache.myfaces.tobago.taglib.decl.HasTreeNodeValue;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/*
 * Date: 06.04.2006
 * Time: 22:32:57
 */
/**
 * Renders a listbox view of a tree.
 */
@Tag(name = "treeListbox")
@BodyContentDescription(anyTagOf = "<f:facet>* <f:actionListener>?")
@Preliminary(
    "Implement a var attribute for the tree like in the sheet (http://issues.apache.org/jira/browse/TOBAGO-18)")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeListbox",
    rendererType = "TreeListbox")
public interface TreeListboxTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered, HasTreeNodeValue,
    HasState, HasIdReference, HasNameReference, IsRequired {
  /**
   * Flag indicating whether or not this component should be render selectable items.
   * Possible values are:
   * <ul>
   * <li><strong>single</strong> : a singleselection tree is rendered</li>
   * <li><strong>singleLeafOnly</strong> : a singleselection tree is rendered,
   * only Leaf's are selectable</li>
   * <li><strong>siblingLeafOnly</strong> : a multiselection tree is rendered,
   * where only sibling Leaf's are selectable</li>
   * </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "single", allowedValues = {"single", "singleLeafOnly", "siblingLeafOnly"})
  void setSelectable(String selectable);


  /**
   * reference to tip value.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setTipReference(String tipReference);
}
