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
 * Created on: 15.02.2002, 17:01:56
 * $Id: DateTag.java 1362 2005-08-15 11:59:30 +0200 (Mon, 15 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIInput;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import static javax.faces.convert.DateTimeConverter.CONVERTER_ID;
import javax.servlet.jsp.JspException;
import java.util.TimeZone;

public class TimeTag extends InputTag implements TimeTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TimeTag.class);

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {

    UIInput component = (UIInput) getComponentInstance();
    // TODO remove this
    /*if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    } */
    // TODO  move to renderkit
    if (component.getConverter() == null) {
      Application application
          = FacesContext.getCurrentInstance().getApplication();
      DateTimeConverter converter
          = (DateTimeConverter) application.createConverter(CONVERTER_ID);
      converter.setPattern("HH:mm");
      converter.setTimeZone(TimeZone.getDefault());
      component.setConverter(converter);
    }

    return super.doEndTag();
  }

  protected void setProperties(UIComponent component) {
    if (label != null) {
      LOG.warn("the label attribute is deprecated in tc:time, " +
          "please use tx:time instead.");
    }
    super.setProperties(component);
  }
}

