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
 * Created on: 19.02.2002, 17:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;


import org.apache.myfaces.tobago.component.UISelectMany;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

public class SelectManyListboxTag extends SelectManyTag implements SelectManyListboxTagDeclaration {

  private static final Log LOG = LogFactory.getLog(SelectManyListboxTag.class);

  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }
  /*
  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    // TODO remove this
    UIComponent facet = component.getFacet(FACET_LAYOUT);
    if (facet == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  } */

  protected void setProperties(UIComponent component) {
    if (label != null) {
      LOG.warn("the label attribute is deprecated in tc:selectManyListbox, " +
          "please use tx:selectManyListbox instead.");
    }
    super.setProperties(component);
  }
}
