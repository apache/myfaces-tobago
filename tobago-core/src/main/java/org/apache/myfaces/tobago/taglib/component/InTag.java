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
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_PASSWORD;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public class InTag extends TextInputTag implements InTagDeclaration {

  private static final Log LOG = LogFactory.getLog(InTag.class);

  private String password;
  private String suggestMethod;

  @Override
  public void release() {
    super.release();
    password = null;
    suggestMethod = null;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    if (label != null) {
      LOG.warn("the label attribute is deprecated in t:in, " +
          "please use tx:in instead.");
    }

    ComponentUtil.setBooleanProperty(component, ATTR_PASSWORD, password);
    setSuggestMethodBinding(component, suggestMethod);
  }

  private void setSuggestMethodBinding(UIComponent component, String suggestMethod) {
    if (suggestMethod != null) {
      if (UIComponentTag.isValueReference(suggestMethod)) {
        final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
            .createMethodBinding(suggestMethod, new Class[]{String.class});
        component.getAttributes().put("suggestMethod", methodBinding);
      } else {
        component.getAttributes().put("suggestMethod", suggestMethod);
      }
    }
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSuggestMethod() {
    return suggestMethod;
  }

  /**
   *
   * @param suggestMethod
   */
  @TagAttribute
  @UIComponentTagAttribute( defaultValue = "none")
  public void setSuggestMethod(String suggestMethod) {
    this.suggestMethod = suggestMethod;
  }
}
