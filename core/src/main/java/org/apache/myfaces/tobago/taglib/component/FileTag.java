package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIFileInput;

import javax.servlet.jsp.JspException;


public class FileTag extends InputTag implements FileTagDeclaration {

  private static final Log LOG = LogFactory.getLog(FileTag.class);

  public int doStartTag() throws JspException {
    if (getLabel() != null) {
      LOG.warn("the label attribute is deprecated in t:in, please use tx:in instead.");
    }

    return super.doStartTag();
  }

  public String getComponentType() {
    return UIFileInput.COMPONENT_TYPE;
  }
}
