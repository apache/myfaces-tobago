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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CONFIRMATION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_GLOBAL_ONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MAX_NUMBER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MAX_SEVERITY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MIN_SEVERITY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ORDER_BY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DETAIL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_SUMMARY;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIMessages;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;


public class MessagesTag extends TobagoTag
    implements MessagesTagDeclaration {

  private String forComponent;
  private String showSummary;
  private String showDetail;
  private String globalOnly;
  private String minSeverity;
  private String maxSeverity;
  private String maxNumber;
  private String orderBy;
  private String confirmation;

  public String getComponentType() {
    return UIMessages.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
    ComponentUtil.setBooleanProperty(component, ATTR_GLOBAL_ONLY, globalOnly);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_SUMMARY, showSummary);
    ComponentUtil.setBooleanProperty(component, ATTR_SHOW_DETAIL, showDetail);
    setSeverityProperty(component, ATTR_MIN_SEVERITY, minSeverity);
    setSeverityProperty(component, ATTR_MAX_SEVERITY, maxSeverity);
    ComponentUtil.setIntegerProperty(component, ATTR_MAX_NUMBER, maxNumber);
    setOrderByProperty(component, ATTR_ORDER_BY, orderBy);
    ComponentUtil.setBooleanProperty(component, ATTR_CONFIRMATION, confirmation);
  }

    private void setSeverityProperty(UIComponent component, String name, String value) {
      if (value != null) {
        if (UIComponentTag.isValueReference(value)) {
          component.setValueBinding(name, ComponentUtil.createValueBinding(value));
        } else {
          component.getAttributes().put(name, FacesMessage.VALUES_MAP.get(value));
        }
      }
    }

    private void setOrderByProperty(UIComponent component, String name, String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, ComponentUtil.createValueBinding(value));
      } else {
        component.getAttributes().put(name, UIMessages.OrderBy.parse(value));
      }
    }
  }

  public void release() {
    super.release();
    forComponent = null;
    showSummary = null;
    showDetail = null;
    minSeverity = null;
    maxSeverity = null;
    maxNumber = null;
    orderBy = null;
  }

  public String getFor() {
    return forComponent;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  public void setGlobalOnly(String globalOnly) {
    this.globalOnly = globalOnly;
  }

  public void setShowSummary(String showSummary) {
    this.showSummary = showSummary;
  }

  public void setShowDetail(String showDetail) {
    this.showDetail = showDetail;
  }

  public void setMinSeverity(String minSeverity) {
    this.minSeverity = minSeverity;
  }

  public void setMaxSeverity(String maxSeverity) {
    this.maxSeverity = maxSeverity;
  }

  public void setMaxNumber(String maxNumber) {
    this.maxNumber = maxNumber;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public void setConfirmation(String confirmation) {
    this.confirmation = confirmation;
  }
}
