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

import org.junit.Assert;
import org.junit.Test;

public class MeasureUnitTest {

  @Test
  public void testDecimal() {
    Assert.assertEquals("13.3px", "" + Measure.valueOf("13.3px"));
    Assert.assertEquals("13.7px", "" + Measure.valueOf("13.7px"));
    Assert.assertEquals("13.3px", "" + Measure.valueOf("13.3"));
    Assert.assertEquals("13.7px", "" + Measure.valueOf("13.7"));
    Assert.assertEquals("0.7px", "" + Measure.valueOf(".7"));
    Assert.assertEquals("0px", "" + Measure.valueOf("0"));
    Assert.assertEquals(null, Measure.valueOf(""));
  }

  @Test
  public void testOther() {
    Assert.assertEquals("13.3cm", "" + Measure.valueOf("13.3cm"));
    Assert.assertEquals("13.7mm", "" + Measure.valueOf("13.7mm"));
    Assert.assertEquals("13.7ex", "" + Measure.valueOf("13.7ex"));
    Assert.assertEquals("13.7em", "" + Measure.valueOf("13.7em"));
    Assert.assertEquals("13.7in", "" + Measure.valueOf("13.7in"));
    Assert.assertEquals("13.7%", "" + Measure.valueOf("13.7%"));
  }

  @Test
  public void testWrong() {
    Assert.assertNull("Not parsable, so get null", Measure.valueOf("13.3xx"));
    Assert.assertNull("Not parsable, so get null", Measure.valueOf("13.3x"));
    Assert.assertNull("Not parsable, so get null", Measure.valueOf("13.3mmm"));
  }

}
