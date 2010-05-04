package org.apache.myfaces.tobago.internal.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTreeNodeValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasVar;

/**
 * Describes a subtree of nodes.
 */
@Tag(name = "treeData")
@BodyContentDescription(anyTagOf = "<tc:treeNode>")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeData",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITreeData",
    rendererType = RendererTypes.TREE_DATA,
    allowedChildComponenents = "org.apache.myfaces.tobago.TreeNode")
public interface TreeDataTagDeclaration extends
    HasIdBindingAndRendered, HasTreeNodeValue, HasVar {

}
