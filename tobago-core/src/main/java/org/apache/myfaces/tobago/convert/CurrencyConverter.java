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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Currency;

/**
 * JSF converter for the java.util.Currency class.
 */
@org.apache.myfaces.tobago.apt.annotation.Converter(forClass = "java.util.Currency")
public class CurrencyConverter implements Converter {

  public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String string)
      throws ConverterException {
    if (StringUtils.isBlank(string)) {
      return null;
    } else {
      return Currency.getInstance(string);
    }
  }

  public String getAsString(final FacesContext facesContext, final UIComponent component, final Object object)
      throws ConverterException {
    if (object == null) {
      return null;
    }
    try {
      return ((Currency) object).getCurrencyCode();
    } catch (final ClassCastException e) {
      throw new ConverterException("object='" + object + "'", e);
    }
  }
}
