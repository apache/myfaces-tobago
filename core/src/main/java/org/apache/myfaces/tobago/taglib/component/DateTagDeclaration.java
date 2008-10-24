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

import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_DATE;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.InputTagDeclaration;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/*
 * Created: Aug 5, 2005 5:03:15 PM
 * User: bommel
 * $Id$
 */
/**
 * Renders a date input field.
 */
@Tag(name = "date")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIDateInput",
    uiComponentBaseClass = "org.apache.myfaces.tobago.component.UIInputBase",
    rendererType = RENDERER_TYPE_DATE,
    allowedChildComponenents = "NONE",
    facets = {
    @Facet(name = Facets.CHANGE,
        description =
            "This facet can contain a UICommand that is invoked in a case of a change event from the component")
        })
public interface DateTagDeclaration
    extends InputTagDeclaration, HasIdBindingAndRendered, IsReadonly,
    IsDisabled, HasConverter, IsInline, HasLabelAndAccessKey,
    HasTip, IsRequired, HasMarkup {

}
