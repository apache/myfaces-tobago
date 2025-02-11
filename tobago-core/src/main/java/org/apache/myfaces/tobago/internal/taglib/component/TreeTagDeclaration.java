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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasVar;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsShowRoot;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsShowRootJunction;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.model.Selectable;

import jakarta.faces.component.UIData;

/**
 * A tree with classical look.
 * Usually used with icons and junction lines to open folder, etc.
 */
@Tag(name = "tree")
@BodyContentDescription(anyTagOf = "<tc:treeNode>")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITree",
    uiComponentFacesClass = "jakarta.faces.component.UIData",
    componentFamily = UIData.COMPONENT_FAMILY,
    rendererType = RendererTypes.TREE,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponents = {
        "org.apache.myfaces.tobago.TreeNode"
    })
public interface TreeTagDeclaration
    extends HasIdBindingAndRendered, HasValue, HasVar, IsVisual,
    IsShowRoot, IsShowRootJunction {

  /**
   * Flag indicating whether or not this component should be render selectable items.
   * Possible values are:
   * <ul>
   * <li><strong>none</strong> : not selectable</li>
   * <li><strong>multi</strong> : a multi section tree is rendered</li>
   * <li><strong>single</strong> : a single section tree is rendered</li>
   * <li><strong>multiLeafOnly</strong> : a multi section tree is rendered,
   * only leaf's are selectable</li>
   * <li><strong>singleLeafOnly</strong> : a single section tree is rendered,
   * only leaf's are selectable</li>
   * </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.Selectable",
      defaultValue = Selectable.MULTI,
      allowedValues = {
          Selectable.NONE,
          Selectable.MULTI,
          Selectable.SINGLE,
          Selectable.MULTI_LEAF_ONLY,
          Selectable.SINGLE_LEAF_ONLY},
      defaultCode = "org.apache.myfaces.tobago.model.Selectable.multi")
  void setSelectable(String selectable);

  /**
   * <strong>ValueBindingExpression</strong> pointing to a object to save the
   * component's state.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.TreeState",
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED,
      generate = false)
  void setState(String state);
}
