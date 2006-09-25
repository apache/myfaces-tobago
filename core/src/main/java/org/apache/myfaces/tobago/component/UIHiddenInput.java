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

import javax.faces.context.FacesContext;


/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 10.02.2006
 * Time: 19:13:51
 */
public class UIHiddenInput extends javax.faces.component.UIInput {

  private boolean inline = true;

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.HiddenInput";

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    inline = (Boolean) values[1];
  }

  public Object saveState(FacesContext context) {
    Object[] values  = new Object[2];
    values[0] = super.saveState(context);
    values[1] = inline;
    return values;
  }
}
