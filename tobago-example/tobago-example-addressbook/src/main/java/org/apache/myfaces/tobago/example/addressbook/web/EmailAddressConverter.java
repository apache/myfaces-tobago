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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.example.addressbook.EmailAddress;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class EmailAddressConverter implements Converter {

  private static final Logger LOG = LoggerFactory.getLogger(EmailAddressConverter.class);

  public Object getAsObject(
      FacesContext facesContext, UIComponent component, String reference) {
    if (reference == null || reference.length() == 0) {
      return null;
    }
    String[] parts = reference.split("@");
    if (parts == null || parts.length != 2) {
      throw new ConverterException(MessageUtils.createErrorMessage(
          "converterEmailParts", facesContext));
    }
    return new EmailAddress(reference);
  }

  public String getAsString(
      FacesContext facesContext, UIComponent component, Object object) {
    return object.toString();
  }

}
