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
 * Created 20.12.2004 11:13:35.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;

public class UIPopup extends UIPanel implements NamingContainer {

  private static final Log LOG = LogFactory.getLog(UIPopup.class);

  private String width;
  private String height;
  private String left;
  private String top;

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Popup";

  public Object saveState(FacesContext facesContext) {
    Object[] saveState = new Object[5];
    saveState[0] = super.saveState(facesContext);
    saveState[1] = width;
    saveState[2] = height;
    saveState[3] = width;
    saveState[4] = top;
    return saveState;
  }



  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    width  =  (String) values[1];
    height = (String) values[2];
    left   = (String) values[3];
    top    = (String) values[4];
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getTop() {
    return top;
  }

  public void setTop(String top) {
    this.top = top;
  }
}
