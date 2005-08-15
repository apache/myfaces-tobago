/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Created 29.07.2003 at 15:09:53.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIPanel;

/**
 * 
 *       Renders a menu bar.<br>
 *       Add menu bar as facet name="menuBar" to page tag or use it anywhere
 *       on page.<br>
 *
 */
@Tag(name="menuBar")
@BodyContentDescription(
    anyClassOf={"org.apache.myfaces.tobago.taglib.component.MenuTag",
    "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag" })
public class MenuBarTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasWidth {

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

}
