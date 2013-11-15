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

package org.apache.myfaces.tobago.internal.taglib.extension;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.JspIdConsumer;

public class TobagoExtensionBodyTagSupport extends BodyTagSupport implements JspIdConsumer {

  protected static final String PREFIX = "tx";
  
  private String jspId;
  private int idSuffix;
    
  protected ValueExpression createStringValueExpression(final String expression) {
    return JspFactory.getDefaultFactory().getJspApplicationContext(pageContext.getServletContext())
        .getExpressionFactory().createValueExpression(pageContext.getELContext(), expression, String.class);
  }
  
  public void setJspId(final String jspId) {
    this.jspId = jspId;
  }

  protected String nextJspId() {
    return jspId + PREFIX + idSuffix++;
  }

  public void release() {
    super.release();
    jspId = null;
    idSuffix = 0;
  }
}
