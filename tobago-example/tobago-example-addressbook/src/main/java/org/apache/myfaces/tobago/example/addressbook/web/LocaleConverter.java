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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Locale;

public class LocaleConverter implements Converter {

  public Object getAsObject(
      FacesContext facesContext, UIComponent component, String value) {
    Locale locale = createLocale(value);
    if (locale == null) {
      throw new ConverterException(MessageUtils.getLocalizedString(
          facesContext, "converterLocaleParserError", value));
    }
    return locale;
  }

  public String getAsString(
      FacesContext facesContext, UIComponent component, Object value) {
    return value == null ? null : value.toString();
  }

  public static Locale createLocale(String value) {
    String[] strings = value.split("_");
    switch (strings.length) {
      case 1:
        return new Locale(strings[0]);
      case 2:
        return new Locale(strings[0], strings[1]);
      case 3:
        return new Locale(strings[0], strings[1], strings[2]);
      default:
        return null;
    }
  }

}
