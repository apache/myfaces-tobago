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



/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 10.02.2006
 * Time: 21:20:10
 * To change this template use File | Settings | File Templates.
 */
public class UICell extends UIPanel {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Cell";

  private Integer spanX = 1;
  private Integer spanY = 1;
  private String scrollbars = "false";

  public Integer getSpanX() {
    return spanX;
  }

  public void setSpanX(Integer spanX) {
    this.spanX = spanX;
  }

  public Integer getSpanY() {
    return spanY;
  }

  public void setSpanY(Integer spanY) {
    this.spanY = spanY;
  }

  public String getScrollbars() {
    return scrollbars;
  }

  public void setScrollbars(String scrollbars) {
    this.scrollbars = scrollbars;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    spanX = (Integer) values[1];
    spanY = (Integer) values[2];
    scrollbars = (String) values[3];
  }

  public Object saveState(FacesContext context) {
    Object[] values  = new Object[4];
    values[0] = super.saveState(context);
    values[1] = spanX;
    values[2] = spanY;
    values[3] = scrollbars;
    return values;
  }
}
