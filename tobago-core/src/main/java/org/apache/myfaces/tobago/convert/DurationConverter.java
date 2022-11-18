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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Converts durations. The duration value in the model is of type long.
 * The string format must have one of this patterns:
 * <ul>
 * <li>hh:MM:ss</li>
 * <li>MM:ss</li>
 * <li>ss</li>
 * </ul>
 * There may be an optional attribute "unit" in the component, which allows to set the unit of the model.
 * The default unit is "millis".
 * <table>
 *   <caption>Examples</caption>
 *   <tr><th>input string</th><th>unit</th><th>resulting long in model</th><th>Remark</th></tr>
 *   <tr><td>1:15</td><td>milli</td><td>75000</td><td></td></tr>
 *   <tr><td>1:15:00</td><td>hour</td><td>1</td><td>Loosing 15 Minutes!</td></tr>
 *   <tr><td>1:15:00</td><td>null</td><td>4500000</td><td></td></tr>
 * </table>
 */
@org.apache.myfaces.tobago.apt.annotation.Converter(id = DurationConverter.CONVERTER_ID)
public class DurationConverter implements Converter {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String CONVERTER_ID = "org.apache.myfaces.tobago.Duration";

  private enum Unit {
    nano, milli, second, minute, hour, day, year
  }

  @Override
  public String getAsString(
      final FacesContext facesContext, final UIComponent component, final Object object)
      throws ConverterException {
    if (object == null || object instanceof String) {
      return (String) object;
    }
    double aDouble = ((Number) object).doubleValue();
    boolean negative = false;
    if (aDouble < 0) {
      negative = true;
      aDouble = -aDouble;
    }
    final double factor = getUnitFactor(component);
    aDouble = aDouble * factor;

    final NumberFormat format = new DecimalFormat("00");
    long value = Double.valueOf(aDouble).longValue();
    final int seconds = (int) (value % 60);
    value = value / 60;
    final int minutes = (int) (value % 60);
    value = value / 60;
    final String string;
    if (value > 0) {
      string = (negative ? "-" : "") + value + ":"
          + format.format(minutes) + ":"
          + format.format(seconds);
    } else {
      string = (negative ? "-" : "") + minutes + ":"
          + format.format(seconds);
    }
    LOG.debug("string = '{}'", string);
    return string;
  }

  @Override
  public Object getAsObject(
      final FacesContext facesContext, final UIComponent component, final String string)
      throws ConverterException {
    final boolean negative = string.indexOf('-') > -1;
    final StringTokenizer tokenizer = new StringTokenizer(string, " :-");
    final List<String> elements = new ArrayList<>();
    while (tokenizer.hasMoreTokens()) {
      elements.add(tokenizer.nextToken());
    }
    final int hours;
    final int minutes;
    final int seconds;
    switch (elements.size()) {
      case 3:
        hours = Integer.parseInt(elements.get(0));
        minutes = Integer.parseInt(elements.get(1));
        seconds = Integer.parseInt(elements.get(2));
        break;
      case 2:
        hours = 0;
        minutes = Integer.parseInt(elements.get(0));
        seconds = Integer.parseInt(elements.get(1));
        break;
      case 1:
        hours = 0;
        minutes = 0;
        seconds = Integer.parseInt(elements.get(0));
        break;
      default:
        throw new ConverterException("Cannot parse string='" + string + "'");
    }
    final double factor = getUnitFactor(component);
    final long value = (long) (((hours * 60L + minutes) * 60L + seconds) / factor);
    if (negative) {
      return -value;
    } else {
      return value;
    }
  }

  private static double getUnitFactor(final UIComponent component) {
    final String unitString = ComponentUtils.getStringAttribute(component, Attributes.unit);
    Unit unit;
    if (unitString != null) {
      try {
        unit = Unit.valueOf(unitString);
      } catch (final Exception e) {
        LOG.warn("Unsupported unit: '{}'", unitString);
        unit = Unit.milli;
      }
    } else {
      unit = Unit.milli;
    }
    switch (unit) {
      case nano:
        return 0.000_000_001;
      default:
      case milli:
        return 0.001;
      case second:
        return 1.0;
      case minute:
        return 60.0;
      case hour:
        return 3_600.0;
      case day:
        return 86_400.0;
      case year:
        return 31_556_736.0;
    }
  }

}
