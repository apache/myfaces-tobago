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

package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.component.AbstractCommandTagDeclaration;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasImage;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTarget;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;

/**
 * Creates a tree node.
 */
@SuppressWarnings("ALL")
@Tag(name = "treeNode")
@BodyContentDescription(anyTagOf = "<tcs:treeNode>* <tcs:treeData>*")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeNode",
    rendererType = "TreeNode",
    facets = {
    @Facet(name = "addendum", description = "Displays an additional component to a node.")})
public interface TreeNodeTagDeclaration
    extends HasIdBindingAndRendered, HasLabel, HasValue, HasMarkup, AbstractCommandTagDeclaration, HasTip, HasTarget,
    HasImage, IsDisabled {

  /**
   * Flag indicating if the subnodes are to be displayed.
   */
  @TagAttribute(type = String.class)
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setExpanded(String expanded);

  /**
   * Flag indicating if the node is marked, and should be displayed in a special way.
   */
  @TagAttribute(type = String.class)
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setMarked(String marked);
}
