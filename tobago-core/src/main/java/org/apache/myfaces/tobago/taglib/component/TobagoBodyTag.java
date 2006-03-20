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

/*
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public abstract class TobagoBodyTag extends TobagoTag implements TobagoBodyTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TobagoBodyTag.class);

  private BodyContent bodyContent;

  public void doInitBody() throws JspException {
  }

  public int doAfterBody() throws JspException {
    return SKIP_BODY;
  }


  public int doEndTag() throws JspException {
    if (LOG.isWarnEnabled()) {
      UIComponent component = getComponentInstance();
      if (component != null && component.getRendersChildren() && !isBodyContentEmpty()) {
        LOG.warn("BodyContent should be empty. Component with id "+ component.getId()
            + " class "+component.getClass().getName() +" content "+ bodyContent.getString()
            + "  Please use the f:verbatim tag for nested content!");
      }
    }
    return super.doEndTag();
  }

  protected boolean isBodyContentEmpty() {
    if (bodyContent != null) {
      String content = bodyContent.getString();
      //bodyContent.clearBody();
      String tmp = content.replace('\n', ' ');
      if (tmp.trim().length() > 0) { // if there are only whitespaces: drop bodyContent
        return false;
      }
    }
    return true;
  }

  protected int getDoStartValue() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  public void release() {
    super.release();
    bodyContent = null;
  }

  public void setBodyContent(BodyContent bodyContent) {
    this.bodyContent = bodyContent;
  }
}

