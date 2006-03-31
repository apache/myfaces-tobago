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
 * Date: 09.02.2006
 * Time: 23:53:37
 * To change this template use File | Settings | File Templates.
 */
public class UIOutput extends javax.faces.component.UIOutput {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Output";
  private boolean escape = true;
  private String markup;
  private String tip;
  private boolean createSpan = true;

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    escape = (Boolean) values[1];
    markup = (String) values[2];
    tip = (String) values[3];
    createSpan = (Boolean) values[4];
   }

  public Object saveState(FacesContext context) {
    Object[] values  = new Object[5];
    values[0] = super.saveState(context);
    values[1] = escape;
    values[2] = markup;
    values[3] = tip;
    values[4] = createSpan;
    return values;
  }

  public boolean isEscape() {
    return escape;
  }

  public void setEscape(boolean escape) {
    this.escape = escape;
  }

  public String getMarkup() {
    return markup;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public boolean isCreateSpan() {
    return createSpan;
  }

  public void setCreateSpan(boolean createSpan) {
    this.createSpan = createSpan;
  }

}
