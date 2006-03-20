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

import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 11.02.2006
 * Time: 14:29:26
 * To change this template use File | Settings | File Templates.
 */
@Tag(name = "form")
@UIComponentTag(UIComponent = "org.apache.myfaces.tobago.component.UIForm")
public interface FormTagDeclaration extends TobagoBodyTagDeclaration,
    HasBinding, HasId {
}
