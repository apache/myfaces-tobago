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

import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;

/**
 * This component creates a representative in a UISheet for each row.
 * The tag must be a subtag of &lt;tc:sheet&gt;
 */
@Tag(name = "row")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIRow",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIRow",
    uiComponentFacesClass = "javax.faces.component.UIColumn",
    rendererType = RendererTypes.ROW,
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CLICK,
            description = "Behavior of a click event.",
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.DBLCLICK)
    })
public interface RowTagDeclaration extends HasIdBindingAndRendered {
}
