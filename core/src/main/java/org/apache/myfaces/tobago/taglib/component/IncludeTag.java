/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasStringValue;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@Tag(name = "include", bodyContent = BodyContent.EMPTY)
@Deprecated()
public class IncludeTag extends TagSupport implements HasId, HasStringValue {

  private static final Log LOG = LogFactory.getLog(IncludeTag.class);

  private String value;


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


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

