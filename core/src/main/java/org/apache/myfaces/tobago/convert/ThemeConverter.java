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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Theme;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@org.apache.myfaces.tobago.apt.annotation.Converter(forClass = "org.apache.myfaces.tobago.context.Theme")
public class ThemeConverter implements Converter {

  private static final Log LOG = LogFactory.getLog(ThemeConverter.class);

  public static final String CONVERTER_ID = "org.apache.myfaces.tobago.Theme";

  public String getAsString(
      FacesContext facesContext, UIComponent component, Object object)
      throws ConverterException {
    try {
      return ((Theme) object).getName();
    } catch (ClassCastException e) {
      throw new ConverterException("object='" + object + "'", e);
    }
  }

  public Object getAsObject(
      FacesContext facesContext, UIComponent component, String string)
      throws ConverterException {
    try {
      return TobagoConfig.getInstance(facesContext).getTheme(string);
    } catch (Exception e) {
      LOG.error("string='" + string + "'", e);
      throw new ConverterException("string='" + string + "'", e);
    }
  }

}
