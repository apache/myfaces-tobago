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

import junit.framework.TestCase;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_UNIT;
import org.apache.myfaces.tobago.component.UIInput;

import javax.faces.convert.Converter;

public class DurationConverterUnitTest extends TestCase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private Converter converter;

// ///////////////////////////////////////////// constructor

  public DurationConverterUnitTest(String string) {
    super(string);
  }

// ///////////////////////////////////////////// code

  protected void setUp() throws Exception {
    super.setUp();
    converter = new DurationConverter();
  }


  public void testFormat() {

    format(null, new Long(1000L), "0:01");
    format("second", new Long(1000L), "16:40");
    format("minute", new Long(-100L), "-1:40:00");
    format("hour", new Long(1L), "1:00:00");
    format("day", new Long(1L), "24:00:00");
    format("year", new Long(1L), "8765:45:36");

  }

  public void testParse() {

    parse(null, new Long(1000L), "0:01");
    parse("second", new Long(1001L), "16:41");
    parse("minute", new Long(-16L), "-16:41");
    parse("hour", new Long(1L), "1:00:00");
    parse("day", new Long(1L), "24:00:00");
    parse("year", new Long(1L), "8765:45:36");
  }

  private void format(String unit, Long aLong, String string) {
    UIInput input = new UIInput();
    String info = "Formatting numbers:"
        + " unit='" + unit + "'"
        + " long='" + aLong + "'";
    String result;
    if (unit != null) {
      input.getAttributes().put(ATTR_UNIT, unit);
    }
    result = converter.getAsString(null, input, aLong);
    assertEquals(info, string, result);
  }

  private void parse(String unit, Long aLong, String string) {
    UIInput input = new UIInput();
    String info = "Parsing numbers:"
        + " unit='" + unit + "'"
        + " string='" + string + "'";
    Long result;
    if (unit != null) {
      input.getAttributes().put(ATTR_UNIT, unit);
    }
    result = (Long) converter.getAsObject(null, input, string);
    assertEquals(info, aLong, result);
  }


// ///////////////////////////////////////////// bean getter + setter

}
