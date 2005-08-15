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
 * Copyright (c) 2002 Atanion GmbH, Germany
 * Created 14.08.2002 at 14:39:25.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIPage;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;


public class FileTag extends InputTag
    implements org.apache.myfaces.tobago.taglib.decl.FileTag {
  // ----------------------------------------------------------- business methods

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    UIPage form = ComponentUtil.findPage(getComponentInstance());
    form.getAttributes().put(ATTR_ENCTYPE, "multipart/form-data");
    return result;
  }

  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  }

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }


  public void setValue(String value) {
    super.setValue(value);
  }
}

