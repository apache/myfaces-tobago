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
import org.apache.myfaces.tobago.component.UIIn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.faces.convert.Converter;

public class DurationConverterUnitTest {

  private Converter converter;

  @Before
  public void setUp() throws Exception {
    converter = new DurationConverter();
  }

  @Test
  public void testFormat() {

    format(null, 1000L, "0:01");
    format("second", 1000L, "16:40");
    format("minute", -100L, "-1:40:00");
    format("hour", 1L, "1:00:00");
    format("day", 1L, "24:00:00");
    format("year", 1L, "8765:45:36");

  }

  @Test
  public void testParse() {

    parse(null, 1000L, "0:01");
    parse("second", 1001L, "16:41");
    parse("minute", -16L, "-16:41");
    parse("hour", 1L, "1:00:00");
    parse("day", 1L, "24:00:00");
    parse("year", 1L, "8765:45:36");
  }

  private void format(final String unit, final Long aLong, final String string) {
    final UIIn input = new UIIn();
    final String info = "Formatting numbers:"
        + " unit='" + unit + "'"
        + " long='" + aLong + "'";
    final String result;
    if (unit != null) {
      input.getAttributes().put(Attributes.unit.getName(), unit);
    }
    result = converter.getAsString(null, input, aLong);
    Assert.assertEquals(info, string, result);
  }

  private void parse(final String unit, final Long aLong, final String string) {
    final UIIn input = new UIIn();
    final String info = "Parsing numbers:"
        + " unit='" + unit + "'"
        + " string='" + string + "'";
    final Long result;
    if (unit != null) {
      input.getAttributes().put(Attributes.unit.getName(), unit);
    }
    result = (Long) converter.getAsObject(null, input, string);
    Assert.assertEquals(info, aLong, result);
  }

}
