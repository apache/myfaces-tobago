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

package org.apache.myfaces.tobago.example.addressbook.web;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageUtils {

  private MessageUtils() {
  }

  public static String getLocalizedString(FacesContext facesContext, String key) {
    ResourceBundle bundle = ResourceBundle.getBundle(
        facesContext.getApplication().getMessageBundle(),
        facesContext.getViewRoot().getLocale());
    return bundle.getString(key);
  }

  public static String getLocalizedString(FacesContext facesContext,
      String key, String value) {
    return MessageFormat.format(
        getLocalizedString(facesContext, key), value);
  }

  public static String getLocalizedString(FacesContext facesContext,
      String key, int value) {
    return MessageFormat.format(
        getLocalizedString(facesContext, key), value);
  }

  public static FacesMessage createErrorMessage(
      String key, FacesContext facesContext) {
    FacesMessage message = new FacesMessage();
    // TODO _detail
    message.setDetail(getLocalizedString(facesContext, key));
    message.setSummary(getLocalizedString(facesContext, key));
    message.setSeverity(FacesMessage.SEVERITY_ERROR);
    return message;
  }

}
