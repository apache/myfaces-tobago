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
 * Created on: 15.02.2002, 17:01:56
 * $Id: DateTag.java 1362 2005-08-15 11:59:30 +0200 (Mon, 15 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.taglib.component;

import static javax.faces.convert.DateTimeConverter.CONVERTER_ID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;

import javax.faces.component.UIComponent;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.servlet.jsp.JspException;
import static org.apache.myfaces.tobago.TobagoConstants.*;

import java.util.TimeZone;

public class TimeTag extends InputTag
    implements org.apache.myfaces.tobago.taglib.decl.TimeTag {

  private static final Log LOG = LogFactory.getLog(TimeTag.class);

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {

    UIInput component = (UIInput) getComponentInstance();
    if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }

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

}

