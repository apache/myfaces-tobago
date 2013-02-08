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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasVar;

import javax.faces.component.UIInput;

/**
 * Describes a sub tree of nodes.
 * The value has to be a javax.swing.tree.TreeNode object to use as rootNode in the tree.
 *
 * @deprecated since 1.6.0. Please use the "var" and "value" attribute of the parent tree tag.
 */
@Deprecated
@Tag(name = "treeData")
@BodyContentDescription(anyTagOf = "<tc:treeNode>")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeData",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITreeData",
    uiComponentFacesClass = "javax.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.TREE_DATA,
    allowedChildComponenents = "org.apache.myfaces.tobago.TreeNode")
public interface TreeDataTagDeclaration extends
    HasIdBindingAndRendered, HasValue, HasVar {
}
