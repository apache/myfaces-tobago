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

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.lang.invoke.MethodHandles;

@FacesConverter(forClass = SolarObject.class)// XXX fixme: is not running with Quarkus!
public class SolarConverter implements Converter<SolarObject> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  @Override
  public SolarObject getAsObject(final FacesContext context, final UIComponent component, final String value)
      throws ConverterException {

    if (astroData == null) { // XXX @Inject doesn't work in some cases
      astroData = CDI.current().select(AstroData.class).get();
    }

    final SolarObject solarObject = value != null ? astroData.find(value) : null;
    LOG.info("{} [String] -> {} [SolarObject]", value, solarObject);
    return solarObject;
  }

  @Override
  public String getAsString(final FacesContext context, final UIComponent component, final SolarObject value)
      throws ConverterException {
    final String result = value.getName();
    LOG.info("{} [SolarObject] -> {} [String]", value, result);
    return result;
  }
}
