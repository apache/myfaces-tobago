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
 * Created on: 19.02.2002, 17:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectMany;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;


public class SelectManyListboxTag extends SelectManyTag
    implements org.apache.myfaces.tobago.taglib.decl.SelectManyListboxTag {

  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    // TODO remove this
    UIComponent facet = component.getFacet(FACET_LAYOUT);
    if (facet == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  }
}
