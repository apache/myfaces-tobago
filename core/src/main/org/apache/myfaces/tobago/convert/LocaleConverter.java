/*
 * Copyright 2002-2005 atanion GmbH.
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
 * All rights reserved. Created 15.04.2004 19:00:46.
 * $Id$
 */
package org.apache.myfaces.tobago.convert;

import org.apache.myfaces.tobago.util.LocaleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Locale;

public class LocaleConverter implements Converter {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public Object getAsObject(
      FacesContext facesContext, UIComponent component, String value) {
    Locale locale = LocaleUtil.createLocale(value);
    if (locale == null) {
      throw new ConverterException("Can't parse " + value + " to a locale.");
    }
    return locale;
  }

  public String getAsString(
      FacesContext facesContext, UIComponent component, Object value) {
    return value.toString();
  }

// ///////////////////////////////////////////// bean getter + setter

}
