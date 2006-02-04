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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ENCTYPE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIPage;

import javax.servlet.jsp.JspException;


public class FileTag extends InputTag implements FileTagDeclaration {

  private static final Log LOG = LogFactory.getLog(FileTag.class);

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    UIPage form = ComponentUtil.findPage(getComponentInstance());
    form.getAttributes().put(ATTR_ENCTYPE, "multipart/form-data");

    if (getLabel() != null) {
      LOG.warn("the label attribute is deprecated in t:in, please use tx:in instead.");
    }

    return result;
  }

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}
