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

import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.layout.OrderBy;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.MessagesTagDeclaration}
 */
public abstract class AbstractUIMessages extends jakarta.faces.component.UIMessages
    implements Visual {

  public List<Item> createMessageList(final FacesContext facesContext) {

    final Iterator<String> clientIds;
    if (isGlobalOnly()) {
      clientIds = Collections.singleton((String) null).iterator();
    } else if (getFor() != null) {
      clientIds = Collections.singleton(getFor()).iterator();
    } else {
      clientIds = facesContext.getClientIdsWithMessages();
    }

    final List<Item> messages = collectMessageList(facesContext, clientIds);

    // todo
    if (OrderBy.severity == getOrderBy()) {
      messages.sort((d1, d2)
          -> d2.getFacesMessage().getSeverity().getOrdinal() - d1.getFacesMessage().getSeverity().getOrdinal());
    }
    return messages;
  }

  private List<Item> collectMessageList(final FacesContext facesContext, final Iterator<String> clientIds) {
    final List<Item> messages = new ArrayList<>();
    while (clientIds.hasNext()) {
      final String clientId = clientIds.next();
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
        final UIComponent component = clientId != null ? facesContext.getViewRoot().findComponent(clientId) : null;
        final String forId;
        if (component instanceof SupportFieldId) {
          forId = ((SupportFieldId) component).getFieldId(facesContext);
        } else {
          forId = clientId;
        }
        messages.add(new Item(forId, facesMessage));
      }
    }
    return messages;
  }

  public static class Item {

    private String forId;
    private FacesMessage facesMessage;

    public Item(final String clientId, final FacesMessage facesMessage) {
      this.forId = clientId;
      this.facesMessage = facesMessage;
    }

    public String getForId() {
      return forId;
    }

    public void setForId(final String forId) {
      this.forId = forId;
    }

    /**
     * @deprecated since Tobago 5.0.0. Please use {@link #setForId}
     */
    @Deprecated
    public String getClientId() {
      return forId;
    }

    /**
     * @deprecated since Tobago 5.0.0. Please use {@link #setForId}
     */
    @Deprecated
    public void setClientId(final String clientId) {
      this.forId = clientId;
    }

    public FacesMessage getFacesMessage() {
      return facesMessage;
    }

    public void setFacesMessage(final FacesMessage facesMessage) {
      this.facesMessage = facesMessage;
    }
  }

  public abstract FacesMessage.Severity getMinSeverity();

  public abstract FacesMessage.Severity getMaxSeverity();

  public abstract Integer getMaxNumber();

  public abstract OrderBy getOrderBy();

  public abstract boolean isConfirmation();
}
