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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;

/**
 * Creates a tree node. This component represents a single node inside a tree structure.
 */
@SuppressWarnings("ALL")
@Tag(name = "treeNode")
@BodyContentDescription(anyTagOf = "<tc:treeIndent>|<tc:treeIcon>|<tc:treeSelect>|<tc:treeLabel>|<tc:treeCommand>")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeNode",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITreeNode",
    rendererType = RendererTypes.TREE_NODE,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.TreeNode",
        "org.apache.myfaces.tobago.TreeData"
    })
public interface
    TreeNodeTagDeclaration
    extends HasIdBindingAndRendered, HasMarkup, HasCurrentMarkup, HasTip, IsDisabled {

  /**
   * Flag indicating if the subnodes are to be displayed.
   * @deprecated since 1.6.0. Please use the state attribute of the tree with a TreeState
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setExpanded(String expanded);

  /**
   * Flag indicating if the node is marked, and should be displayed in a special way.
   * @deprecated since 1.6.0. Please use the state attribute of the tree with a TreeState
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setMarked(String marked);

  /**
   * Flag indicating if the node is selected (only possible, when the tree component allows it).
   * @deprecated since 1.6.0. Please use <code>&lt;tc:treeSelect></code>
   */
  @Deprecated
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setSelected(String selected);

  /**
   * Method binding representing a expansionListener method that ....
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_BINDING_REQUIRED,
      methodSignature = "org.apache.myfaces.tobago.event.TreeExpansionEvent")
  void setTreeExpansionListener(String treeExpansionListener);

  /**
   * Method binding representing a markedListener method that ....
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_BINDING_REQUIRED,
      methodSignature = "org.apache.myfaces.tobago.event.TreeMarkedEvent")
  void setTreeMarkedListener(String treeMarkedListener);

}
