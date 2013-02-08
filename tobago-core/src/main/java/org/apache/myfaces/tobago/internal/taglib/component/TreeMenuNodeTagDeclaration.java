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

import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;

import javax.faces.component.UIColumn;

/**
 * Render for tree command inside of a tree menu.
 */
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeNode",
    uiComponentFacesClass = "javax.faces.component.UIColumn",
    generate = false,
    componentFamily = UIColumn.COMPONENT_FAMILY,
    rendererType = RendererTypes.TREE_MENU_NODE)
public interface TreeMenuNodeTagDeclaration extends TreeNodeTagDeclaration {
}
