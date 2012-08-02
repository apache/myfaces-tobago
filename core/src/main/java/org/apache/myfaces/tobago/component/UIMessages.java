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

package org.apache.myfaces.tobago.component;

import org.apache.commons.collections.iterators.SingletonIterator;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class UIMessages extends javax.faces.component.UIMessages {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Messages";

  private FacesMessage.Severity minSeverity;
  private FacesMessage.Severity maxSeverity;
  private Integer maxNumber;
  private OrderBy orderBy;
  private String forValue;
  private Boolean confirmation;

  public List<Item> createMessageList(FacesContext facesContext) {

    List<Item> messages = createMessageListInternal(facesContext);

    // todo
    if (OrderBy.SEVERITY.equals(orderBy)) {
      // sort
      Collections.sort(messages, new ItemComparator());
    }
    return messages;
  }

  public int getMessageListCount(final FacesContext facesContext) {
    return createMessageListInternal(facesContext).size();
  }

  private List<Item> createMessageListInternal(FacesContext facesContext) {
    Iterator clientIds;
    if (isGlobalOnly()) {
      clientIds = new SingletonIterator(null);
    } else if (getFor() != null) {
      clientIds = new SingletonIterator(getFor());
    } else {
      clientIds = facesContext.getClientIdsWithMessages();
    }

    return collectMessageList(facesContext, clientIds);
  }

  private List<Item> collectMessageList(FacesContext facesContext, Iterator clientIds) {
    List<Item> messages = new ArrayList<Item>();
    while(clientIds.hasNext()) {
      String clientId = (String) clientIds.next();
      Iterator<FacesMessage> i = facesContext.getMessages(clientId);
      while (i.hasNext()) {
        FacesMessage facesMessage = i.next();
        if (maxNumber != null && messages.size() >= maxNumber) {
          return messages;
        }
        if (facesMessage.getSeverity().getOrdinal() < getMinSeverity().getOrdinal()) {
          continue;
        }
        if (facesMessage.getSeverity().getOrdinal() > getMaxSeverity().getOrdinal()) {
          continue;
        }
        messages.add(new Item(clientId, facesMessage));
      }
    }
    return messages;
  }

  public static class Item {

    private String clientId;
    private FacesMessage facesMessage;

    public Item(String clientId, FacesMessage facesMessage) {
      this.clientId = clientId;
      this.facesMessage = facesMessage;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }

    public FacesMessage getFacesMessage() {
      return facesMessage;
    }

    public void setFacesMessage(FacesMessage facesMessage) {
      this.facesMessage = facesMessage;
    }
  }

  public static class ItemComparator implements Comparator<Item> {
    public int compare(Item item1, Item item2) {
      return item2.getFacesMessage().getSeverity().getOrdinal() - item1.getFacesMessage().getSeverity().getOrdinal();
    }
  }

  public FacesMessage.Severity getMinSeverity() {
    if (minSeverity != null) {
      return minSeverity;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_MIN_SEVERITY);
    if (vb != null) {
      return (FacesMessage.Severity) vb.getValue(getFacesContext());
    } else {
      return FacesMessage.SEVERITY_INFO;
    }
  }

  public void setMinSeverity(FacesMessage.Severity minSeverity) {
    this.minSeverity = minSeverity;
  }

  public FacesMessage.Severity getMaxSeverity() {
    if (maxSeverity != null) {
      return maxSeverity;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_MAX_SEVERITY);
    if (vb != null) {
      return (FacesMessage.Severity) vb.getValue(getFacesContext());
    } else {
      return FacesMessage.SEVERITY_FATAL;
    }
  }

  public void setMaxSeverity(FacesMessage.Severity maxSeverity) {
    this.maxSeverity = maxSeverity;
  }

  public Integer getMaxNumber() {
    if (maxNumber != null) {
      return maxNumber;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_MAX_NUMBER);
    if (vb != null) {
      Number number = (Number) vb.getValue(getFacesContext());
      if (number != null) {
        return Integer.valueOf(number.intValue());
      }
    }
    return null;
  }

  public void setMaxNumber(Integer maxNumber) {
    this.maxNumber = maxNumber;
  }

  public OrderBy getOrderBy() {
    if (orderBy != null) {
      return orderBy;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_ORDER_BY);
    if (vb != null) {
      return (OrderBy) vb.getValue(getFacesContext());
    } else {
      return OrderBy.OCCURENCE;
    }
  }

  public void setOrderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
  }

  public void setFor(String forValue) {
    this.forValue = forValue;
  }

  public String getFor() {
    if (forValue != null) {
      return forValue;
    }
    ValueBinding vb = getValueBinding("for");
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public boolean isConfirmation() {
    if (confirmation != null) {
      return confirmation;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_CONFIRMATION);
    if (vb != null) {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    } else {
      return false;
    }
  }

  public void setConfirmation(boolean confirmation) {
    this.confirmation = confirmation;
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] values = new Object[7];
    values[0] = super.saveState(context);
    values[1] = minSeverity;
    values[2] = maxSeverity;
    values[3] = maxNumber;
    values[4] = orderBy;
    values[5] = forValue;
    values[6] = confirmation;
    return values;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    minSeverity = (FacesMessage.Severity) values[1];
    maxSeverity = (FacesMessage.Severity) values[2];
    maxNumber = (Integer) values[3];
    orderBy = (OrderBy) values[4];
    forValue = (String) values[5];
    confirmation = (Boolean) values[6];
  }

  public enum OrderBy {

    OCCURENCE,
    SEVERITY;

    public static final String OCCURENCE_STRING = "occurence";
    public static final String OCCURRENCE_STRING = "occurrence";
    public static final String SEVERITY_STRING = "severity";

    public static OrderBy parse(String key) {
      if (OCCURENCE_STRING.equals(key)) {
        Deprecation.LOG.warn("Please use '" + OCCURRENCE_STRING + "' instead of '" + OCCURENCE_STRING + "'");
      }
      if (OCCURRENCE_STRING.equals(key)) {
        key = OCCURENCE_STRING;
      }
      return valueOf(key.toUpperCase());
    }

  }
}
