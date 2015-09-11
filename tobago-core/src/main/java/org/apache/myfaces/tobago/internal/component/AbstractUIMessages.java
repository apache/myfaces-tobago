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

package org.apache.myfaces.tobago.internal.component;

import org.apache.commons.collections.iterators.SingletonIterator;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.OrderBy;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractUIMessages extends javax.faces.component.UIMessages
    implements LayoutComponent {

  public List<Item> createMessageList(final FacesContext facesContext) {

    final Iterator clientIds;
    if (isGlobalOnly()) {
      clientIds = new SingletonIterator(null);
    } else if (getFor() != null) {
      clientIds = new SingletonIterator(getFor());
    } else {
      clientIds = facesContext.getClientIdsWithMessages();
    }

    final List<Item> messages = collectMessageList(facesContext, clientIds);

    // todo
    if (OrderBy.severity == getOrderBy()) {
      // sort
      Collections.sort(messages, new ItemComparator());
    }
    return messages;
  }

  private List<Item> collectMessageList(final FacesContext facesContext, final Iterator clientIds) {
    final List<Item> messages = new ArrayList<Item>();
    while(clientIds.hasNext()) {
      final String clientId = (String) clientIds.next();
      final Iterator<FacesMessage> i = facesContext.getMessages(clientId);
      while (i.hasNext()) {
        final FacesMessage facesMessage = i.next();
        if (getMaxNumber() != null && messages.size() >= getMaxNumber()) {
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

    public Item(final String clientId, final FacesMessage facesMessage) {
      this.clientId = clientId;
      this.facesMessage = facesMessage;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(final String clientId) {
      this.clientId = clientId;
    }

    public FacesMessage getFacesMessage() {
      return facesMessage;
    }

    public void setFacesMessage(final FacesMessage facesMessage) {
      this.facesMessage = facesMessage;
    }
  }

  public static class ItemComparator implements Comparator<Item> {
    public int compare(final Item item1, final Item item2) {
      return item2.getFacesMessage().getSeverity().getOrdinal() - item1.getFacesMessage().getSeverity().getOrdinal();
    }
  }

  public abstract FacesMessage.Severity getMinSeverity();

  public abstract FacesMessage.Severity getMaxSeverity();

  public abstract Integer getMaxNumber();

  public abstract OrderBy getOrderBy();

/* TBD: if we support JSF 1.2 whe have to do something here.
  public abstract String getFor();
*/

}
