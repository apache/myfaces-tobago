package org.apache.myfaces.tobago.example.demo.model;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 01.08.2006
 * Time: 20:04:26
 */
public class SalutationConverter implements Converter {

  public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
    return Salutation.getSalutation(value);
  }

  public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
    if (value instanceof Salutation) {
      return ((Salutation)value).getKey();
    }
    return "";
  }
}
