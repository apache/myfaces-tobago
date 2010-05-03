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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.InputTagDeclaration;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/**
 * Render a multi selection option listbox.
 */
@Tag(name = "selectManyListbox")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectManyListbox",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectMany",
    rendererType = RendererTypes.SELECT_MANY_LISTBOX,
    allowedChildComponenents = {"javax.faces.SelectItem", "javax.faces.SelectItems"})

public interface SelectManyListboxTagDeclaration extends InputTagDeclaration, HasId, IsDisabled,
    IsInline, HasLabelAndAccessKey, IsRendered, HasBinding, HasTip, IsReadonly, HasConverter, IsRequired, HasMarkup {
}
