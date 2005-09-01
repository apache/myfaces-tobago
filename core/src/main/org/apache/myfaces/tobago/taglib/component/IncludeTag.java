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
 * Created Oct 31, 2002 at 1:24:42 PM.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasStringValue;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@Tag(name="include", bodyContent=BodyContent.EMPTY)
public class IncludeTag extends TagSupport implements HasId, HasStringValue {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(IncludeTag.class);

// ----------------------------------------------------------------- attributes

  private String value;

// ----------------------------------------------------------- business methods

  public int doStartTag() throws JspException {
    String pageName = null;
    try {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      if (UIComponentTag.isValueReference(value)) {
        ValueBinding valueBinding
            = facesContext.getApplication().createValueBinding(value);
        pageName = (String) valueBinding.getValue(facesContext);
      } else {
        pageName = value;
      }

      pageName = ResourceManagerFactory.getResourceManager(facesContext)
          .getJsp(facesContext.getViewRoot(), pageName);

      if (LOG.isDebugEnabled()) {
        LOG.debug("include start pageName = '" + pageName + "'");
      }
      pageContext.include(pageName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("include end   pageName = '" + pageName + "'");
      }
    } catch (Throwable e) {
      LOG.error("pageName = '" + pageName + "' " + e, e);
      throw new JspException(e);
    }
    return super.doStartTag();
  }

  public void release() {
    value = null;
    super.release();
  }

// ------------------------------------------------------------ getter + setter

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

