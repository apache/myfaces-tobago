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

import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasBorder;
import org.apache.myfaces.tobago.taglib.decl.HasCellspacing;
import org.apache.myfaces.tobago.taglib.decl.HasMargin;
import org.apache.myfaces.tobago.taglib.decl.HasMargins;
import org.apache.myfaces.tobago.taglib.decl.HasColumnLayout;
import org.apache.myfaces.tobago.taglib.decl.HasRowLayout;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 14.03.2006
 * Time: 16:58:13
 * To change this template use File | Settings | File Templates.
 */

@Tag(name = "gridLayout", bodyContent = BodyContent.EMPTY)
@UIComponentTag(UIComponent = "org.apache.myfaces.tobago.component.UIGridLayout")
public interface GridLayoutTagDeclaration extends TobagoTagDeclaration, HasId, HasBorder, HasCellspacing, HasMargin,
    HasMargins, HasColumnLayout, HasRowLayout, HasBinding {
}
