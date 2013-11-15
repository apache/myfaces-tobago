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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@org.apache.myfaces.tobago.apt.annotation.Converter(id = DurationConverter.CONVERTER_ID)
public class DurationConverter implements Converter {

  private static final Logger LOG = LoggerFactory.getLogger(DurationConverter.class);

  public static final String CONVERTER_ID = "org.apache.myfaces.tobago.Duration";

  private static final String NANO = "nano";
  private static final String MILLI = "milli";
  private static final String SECOND = "second";
  private static final String MINUTE = "minute";
  private static final String HOUR = "hour";
  private static final String DAY = "day";
  private static final String YEAR = "year";

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
    if (LOG.isDebugEnabled()) {
      LOG.debug("string = '{}'", string);
    }
    return string;
  }

  public Object getAsObject(
      final FacesContext facesContext, final UIComponent component, final String string)
      throws ConverterException {
    final boolean negative = string.indexOf('-') > -1;
    final StringTokenizer tokenizer = new StringTokenizer(string, " :-");
    final List elements = new ArrayList();
    while (tokenizer.hasMoreElements()) {
      elements.add(tokenizer.nextElement());
    }
    int hours = 0;
    final int minutes;
    final int seconds;
    switch (elements.size()) {
      case 3:
        hours = Integer.parseInt((String) elements.get(0));
        minutes = Integer.parseInt((String) elements.get(1));
        seconds = Integer.parseInt((String) elements.get(2));
        break;
      case 2:
        minutes = Integer.parseInt((String) elements.get(0));
        seconds = Integer.parseInt((String) elements.get(1));
        break;
      default:
        throw new ConverterException("Cannot parse string='" + string + "'");
    }
    final double factor = getUnitFactor(component);
    final long value = (long) (((hours * 60L + minutes) * 60L + seconds) / factor);
    if (negative) {
      return Long.valueOf(-value);
    } else {
      return Long.valueOf(value);
    }
  }

  private static double getUnitFactor(final UIComponent component) {
    String unit = null;
    if (component != null) {
      unit = (String) component.getAttributes().get(Attributes.UNIT);
    }
    final double factor;
    if (unit == null) {
      factor = 0.001;
    } else if (NANO.equals(unit)) {
      factor = 0.000000001;
    } else if (MILLI.equals(unit)) {
      factor = 0.001;
    } else if (SECOND.equals(unit)) {
      factor = 1.0;
    } else if (MINUTE.equals(unit)) {
      factor = 60.0;
    } else if (HOUR.equals(unit)) {
      factor = 3600.0;
    } else if (DAY.equals(unit)) {
      factor = 86400.0;
    } else if (YEAR.equals(unit)) {
      factor = 31556736.0;
    } else {
      LOG.warn("Unsupported unit: '" + unit + "'");
      factor = 0.001;
    }
    return factor;
  }

}
