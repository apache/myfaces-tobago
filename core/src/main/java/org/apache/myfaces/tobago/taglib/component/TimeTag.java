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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UITimeInput;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

public class TimeTag extends InputTag implements TimeTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TimeTag.class);

  public String getComponentType() {
    return UITimeInput.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {


    return super.doEndTag();
  }

  protected void setProperties(UIComponent component) {
    if (getLabel() != null) {
      LOG.warn("the label attribute is deprecated in tc:time, please use tx:time instead.");
    }
    super.setProperties(component);
  }
}

