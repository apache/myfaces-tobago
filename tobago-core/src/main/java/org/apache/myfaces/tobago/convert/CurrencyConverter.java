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

package org.apache.myfaces.tobago.convert;

import org.apache.myfaces.tobago.internal.util.StringUtils;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import java.util.Currency;

/**
 * JSF converter for the {@link java.util.Currency} class.
 */
@org.apache.myfaces.tobago.apt.annotation.Converter(forClass = "java.util.Currency")
public class CurrencyConverter implements Converter<Currency> {

  @Override
  public Currency getAsObject(final FacesContext facesContext, final UIComponent component, final String string)
      throws ConverterException {
    if (StringUtils.isBlank(string)) {
      return null;
    } else {
      return Currency.getInstance(string);
    }
  }

  @Override
  public String getAsString(final FacesContext facesContext, final UIComponent component, final Currency currency)
      throws ConverterException {
    if (currency == null) {
      return null;
    }
    try {
      return currency.getCurrencyCode();
    } catch (final ClassCastException e) {
      throw new ConverterException("object='" + currency + "'", e);
    }
  }
}
