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

public class LayoutTokensUnitTest {

  @Test
  public void testIsPixelToken() {
    Assert.assertTrue(LayoutTokens.isPixelToken("120px"));
  }

  @Test
  public void testIsPercentToken() {
    Assert.assertTrue(LayoutTokens.isPercentToken("50%"));
  }

  @Test
  public void testIsRelativeToken() {
    Assert.assertTrue(LayoutTokens.isRelativeToken("3*"));
  }

  @Test
  public void testIsNumberAndSuffix() {
    Assert.assertTrue(LayoutTokens.isNumberAndSuffix("34cm", "cm"));
    Assert.assertFalse(LayoutTokens.isNumberAndSuffix("acm", "cm"));
    Assert.assertFalse(LayoutTokens.isNumberAndSuffix("cm", "cm"));
  }

  @Test
  public void testParseToken() {
    Assert.assertEquals(AutoLayoutToken.INSTANCE, LayoutTokens.parseToken(null));
    Assert.assertEquals(RelativeLayoutToken.DEFAULT_INSTANCE, LayoutTokens.parseToken("*"));
    Assert.assertEquals(new RelativeLayoutToken(3), LayoutTokens.parseToken("3*"));
    Assert.assertEquals(new PercentLayoutToken(33), LayoutTokens.parseToken("33%"));
    Assert.assertEquals(new PixelLayoutToken(120), LayoutTokens.parseToken("120px"));
    Assert.assertEquals(new PixelLayoutToken(0), LayoutTokens.parseToken("0px"));
  }
}
