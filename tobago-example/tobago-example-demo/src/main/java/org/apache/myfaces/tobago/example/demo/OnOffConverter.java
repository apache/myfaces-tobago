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

package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import java.lang.invoke.MethodHandles;

/**
 * Example of a boolean converter with a string representation in the model ("on" and "off").
 */
public class OnOffConverter implements Converter<String> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public String getAsObject(final FacesContext facesContext, final UIComponent component, final String value)
      throws ConverterException {
    final String result = Boolean.parseBoolean(value) ? "on" : "off";
    LOG.info("Got value = '" + value + "'. Result = '" + result + "'");
    return result;
  }

  @Override
  public String getAsString(final FacesContext facesContext, final UIComponent component, final String value)
      throws ConverterException {
    final String result = "on".equals(value) ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    LOG.info("Got value = '" + value + "'. Result: '" + result + "'");
    return result;
  }
}
