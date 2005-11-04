/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 09.12.2003 15:52:53.
 * $Id$
 */
package org.apache.myfaces.tobago.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Currency;

public class CurrencyConverter implements Converter {

// ----------------------------------------------------------------- interfaces

// ---------------------------- interface Converter

  public Object getAsObject(FacesContext facesContext, UIComponent component,
      String string)
      throws ConverterException {
    return Currency.getInstance(string);
  }

  public String getAsString(FacesContext facesContext, UIComponent component,
      Object object)
      throws ConverterException {
    try {
      return ((Currency) object).getCurrencyCode();
    } catch (ClassCastException e) {
      throw new ConverterException("object='" + object + "'", e);
    }
  }
}

