package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasBorder;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasMargin;
import org.apache.myfaces.tobago.taglib.decl.HasMargins;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasSpacing;

/**
 * XXX Warning: Still in progress! Please do not use it until other announcement.
 * Renders a FlowLayout that positions the content components in there natural order.
 */
@Tag(name = "flowLayout", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFlowLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout",
    rendererType = RendererTypes.FLOW_LAYOUT,
    componentFamily = "org.apache.myfaces.tobago.FlowLayout",
    allowedChildComponenents = "NONE", isLayout = true)
public interface FlowLayoutTagDeclaration extends HasId, HasBorder, HasSpacing, HasMargin,
    HasMargins, HasBinding, HasMarkup {
}
