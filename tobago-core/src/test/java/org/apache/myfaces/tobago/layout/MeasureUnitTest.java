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

package org.apache.myfaces.tobago.layout;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MeasureUnitTest {

  @Test
  public void testDecimal() {
    Assertions.assertEquals("13.3px", "" + Measure.valueOf("13.3px"));
    Assertions.assertEquals("13.7px", "" + Measure.valueOf("13.7px"));
    Assertions.assertEquals("13.3px", "" + Measure.valueOf("13.3"));
    Assertions.assertEquals("13.7px", "" + Measure.valueOf("13.7"));
    Assertions.assertEquals("0.7px", "" + Measure.valueOf(".7"));
    Assertions.assertEquals("0px", "" + Measure.valueOf("0"));
    Assertions.assertEquals(null, Measure.valueOf(""));
  }

  @Test
  public void testOther() {
    Assertions.assertEquals("13.3cm", "" + Measure.valueOf("13.3cm"));
    Assertions.assertEquals("13.7mm", "" + Measure.valueOf("13.7mm"));
    Assertions.assertEquals("13.7ex", "" + Measure.valueOf("13.7ex"));
    Assertions.assertEquals("13.7em", "" + Measure.valueOf("13.7em"));
    Assertions.assertEquals("13.7in", "" + Measure.valueOf("13.7in"));
    Assertions.assertEquals("13.7ch", "" + Measure.valueOf("13.7ch"));
    Assertions.assertEquals("13.7rem", "" + Measure.valueOf("13.7rem"));
    Assertions.assertEquals("13.7vw", "" + Measure.valueOf("13.7vw"));
    Assertions.assertEquals("13.7vh", "" + Measure.valueOf("13.7vh"));
    Assertions.assertEquals("13.7vmin", "" + Measure.valueOf("13.7vmin"));
    Assertions.assertEquals("13.7vmax", "" + Measure.valueOf("13.7vmax"));
    Assertions.assertEquals("13.7%", "" + Measure.valueOf("13.7%"));
    Assertions.assertEquals("13fr", "" + Measure.valueOf("13*"));
    Assertions.assertEquals("13fr", "" + Measure.valueOf("13fr"));
    Assertions.assertEquals("auto", "" + Measure.valueOf("auto"));
    Assertions.assertEquals("13fr", "" + Measure.valueOf("13*"));
    Assertions.assertEquals("1fr", "" + Measure.valueOf("*"));
    Assertions.assertEquals("1seg", "" + Measure.valueOf("1seg"));
  }

  @Test
  public void testWrong() {
    Assertions.assertNull(Measure.valueOf("13.3xx"), "Not parsable, so get null");
    Assertions.assertNull(Measure.valueOf("13.3x"), "Not parsable, so get null");
    Assertions.assertNull(Measure.valueOf("13.3mmm"), "Not parsable, so get null");
  }

}
