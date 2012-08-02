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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DETAIL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_SUMMARY;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;

/**
 * @deprecated Please use MessagesRenderer with maxNumber="1"
 */
@Deprecated
public class MessageTag extends TobagoTag
    implements MessageTagDeclaration {

  private String forComponent;
  private String showSummary;
  private String showDetail;

  public String getComponentType() {
    return UIMessage.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_SUMMARY, showSummary);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_DETAIL, showDetail);
  }

  public void release() {
    super.release();
    forComponent = null;
    showSummary = null;
    showDetail = null;
  }

  public String getFor() {
    return forComponent;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  public void setShowSummary(String showSummary) {
    this.showSummary = showSummary;
  }

  public void setShowDetail(String showDetail) {
    this.showDetail = showDetail;
  }
}
