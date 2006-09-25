package org.apache.myfaces.tobago.component;

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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ENCTYPE;

import javax.faces.component.UIComponent;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 10.02.2006
 * Time: 19:02:13
 */
public class UIFileInput extends javax.faces.component.UIInput {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.FileInput";

  public void setParent(UIComponent uiComponent) {
    super.setParent(uiComponent);
    UIPage form = ComponentUtil.findPage(uiComponent);
    if (form != null) {
      form.getAttributes().put(ATTR_ENCTYPE, "multipart/form-data");
    }
  }

}
