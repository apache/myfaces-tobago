package org.apache.myfaces.tobago.taglib.sandbox;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
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


import org.apache.myfaces.tobago.taglib.component.TobagoTag;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 18, 2006
 * Time: 8:02:34 PM
 */

/**
 * Renders a separator.
 */

@Tag(name = "separator")
@UIComponentTag(rendererType = "Separator",
    uiComponent = "org.apache.myfaces.tobago.component.UISeparator")

public class SeparatorTag  extends TobagoTag implements HasIdBindingAndRendered{

   public String getComponentType() {
    return UISeparator.COMPONENT_TYPE;
  }

}
