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
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasImage;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWithAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTarget;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;

/*
 * Date: 11.02.2006
 * Time: 13:16:47
 */

/**
 * Renders a link element.
 */
@Tag(name = "link", tagExtraInfoClassName = "org.apache.myfaces.tobago.taglib.component.CommandTagExtraInfo")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UILinkCommand",
    rendererType = "Link",
    facets = {
      @Facet(name = "confirmation", description = "Contains a UIOutput instance with the confirmation message."),
      @Facet(name = "popup", description = "Contains a UIPopup instance.")})
public interface LinkTagDeclaration extends AbstractCommandTagDeclaration,
    HasIdBindingAndRendered, HasLabel, HasLabelWithAccessKey, IsDisabled, IsInline,
    HasCommandType, HasTarget, HasTip, HasImage, HasMarkup, HasTabIndex {
}
