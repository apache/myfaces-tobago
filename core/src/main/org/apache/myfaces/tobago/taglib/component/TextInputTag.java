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
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/**
 * User: weber
 * Date: Apr 18, 2005
 * Time: 5:04:48 PM
 */
public class TextInputTag extends InputTag implements org.apache.myfaces.tobago.taglib.decl.TextInputTag {

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    // TODO remove this
    if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  }
}
