package org.apache.myfaces.tobago.taglib.component;
/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created 28.04.2003 at 14:50:02.
 * $Id$
 */

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

/**
 * Intended for use in situations when only one UIComponent child can be
 * nested, such as in the case of facets.
 */
@Tag(name = "panel")
public class PanelTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasDimension {

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

}

