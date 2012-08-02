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

import java.util.Arrays;

public class MeasureUnitTest {

  private static Measure px(int pixel) {
    return Measure.valueOf(pixel);
  }

  @Test
  public void testMinList() {
    Assert.assertEquals(px(5), Measure.min(Arrays.asList(px(5), px(10), px(20))));
    Assert.assertEquals(px(5), Measure.min(Arrays.asList(px(5), null, px(20))));
    Assert.assertEquals(Measure.MAX, Measure.min(Arrays.asList((Measure) null, null, null)));
    Assert.assertEquals(Measure.MAX, Measure.min(Arrays.<Measure>asList()));
  }

  @Test
  public void testMaxList() {
    Assert.assertEquals(px(20), Measure.max(Arrays.asList(px(5), px(10), px(20))));
    Assert.assertEquals(px(20), Measure.max(Arrays.asList(px(5), null, px(20))));
    Assert.assertEquals(Measure.ZERO, Measure.max(Arrays.asList((Measure) null, null, null)));
    Assert.assertEquals(Measure.ZERO, Measure.max(Arrays.<Measure>asList()));
  }

  @Test
  public void testMin2() {
    Assert.assertEquals(px(5), Measure.min(px(5), px(10)));
    Assert.assertEquals(px(10), Measure.min(null, px(10)));
    Assert.assertEquals(px(5), Measure.min(px(5), null));
    Assert.assertEquals(Measure.MAX, Measure.min(null, null));
  }

  @Test
  public void testMax2() {
    Assert.assertEquals(px(10), Measure.max(px(5), px(10)));
    Assert.assertEquals(px(10), Measure.max(null, px(10)));
    Assert.assertEquals(px(5), Measure.max(px(5), null));
    Assert.assertEquals(Measure.ZERO, Measure.max(null, null));
  }
}
