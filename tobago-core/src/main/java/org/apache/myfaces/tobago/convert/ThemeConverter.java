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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import java.lang.invoke.MethodHandles;

@org.apache.myfaces.tobago.apt.annotation.Converter(forClass = "org.apache.myfaces.tobago.context.Theme")
public class ThemeConverter implements Converter<Theme> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String CONVERTER_ID = "org.apache.myfaces.tobago.Theme";

  @Override
  public String getAsString(
      final FacesContext facesContext, final UIComponent component, final Theme theme)
      throws ConverterException {
    try {
      return theme.getName();
    } catch (final ClassCastException e) {
      throw new ConverterException("object='" + theme + "'", e);
    }
  }

  @Override
  public Theme getAsObject(
      final FacesContext facesContext, final UIComponent component, final String string)
      throws ConverterException {
    try {
      return TobagoConfig.getInstance(facesContext).getTheme(string);
    } catch (final Exception e) {
      LOG.error("string='" + string + "'", e);
      throw new ConverterException("string='" + string + "'", e);
    }
  }

}
