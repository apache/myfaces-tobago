package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasHeight;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;

/*
 * Created: Aug 5, 2005 5:58:55 PM
 * User: bommel
 * $Id: $
 */
/**
 * Render a multi selection option listbox.
 */
@Tag(name = "selectManyListbox")
@UIComponentTag(ComponentType = "org.apache.myfaces.tobago.SelectMany",
    UIComponent = "org.apache.myfaces.tobago.component.UISelectMany",
    RendererType = "SelectManyListbox")

public interface SelectManyListboxTagDeclaration
    extends SelectManyTagDeclaration, HasId, HasValue, IsDisabled, 
    HasHeight, IsInline, HasLabelAndAccessKey, IsRendered, HasBinding, HasTip {

}
