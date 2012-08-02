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
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIProgress;

import javax.faces.component.UIComponent;


public class ProgressTag extends BeanTag implements ProgressTagDeclaration {

  private String markup;
  private String tip;

  public String getComponentType() {
    return UIProgress.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setMarkup(component, markup);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
  }

  public void release() {
    super.release();
    markup = null;
    tip = null;
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
}
