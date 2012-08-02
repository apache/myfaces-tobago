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

package org.apache.myfaces.tobago.taglib.component;

/*
 * Created 28.04.2003 at 14:50:02.
 * $Id$
 */


import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPanel;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.tagext.BodyTag;

// Some Weblogic versions need explicit 'implements' for BodyTag
public class PanelTag extends TobagoBodyTag
    implements BodyTag, PanelTagDeclaration {

  private String markup;
  private String tip;

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    markup = null;
    tip = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setMarkup(component, markup);
    ComponentUtil.setStringProperty(component, TobagoConstants.ATTR_TIP, tip);
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}
