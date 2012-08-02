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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;

/*
 * Created: Aug 5, 2005 6:05:28 PM
 * User: bommel
 * $Id$
 */

/**
 * Render a single selection dropdown list.
 */
@Tag(name = "selectOneChoice")
@BodyContentDescription(anyTagOf = "(<f:selectItems>|<f:selectItem>|<tc:selectItem>)+ <f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectOne",
    rendererType = "SelectOneChoice",
    facets = {
    @Facet(name = "click",
        description =
            "This facet can contain a UICommand that is invoked in case of a click event from the component"),
    @Facet(name = "change",
        description =
            "This facet can contain a UICommand that is invoked in case of a change event from the component")
        })
public interface SelectOneChoiceTagDeclaration
    extends SelectOneTagDeclaration, HasId, IsDisabled,
    IsReadonly, IsInline, HasLabelAndAccessKey,
    IsRendered, HasConverter, HasBinding, HasTip {

  /**
   * Flag indicating that selecting an Item representing a value is required.
   * If an SelectItem was choosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute(type = String.class)
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setRequired(String required);
}
