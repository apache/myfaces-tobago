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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RangeParserUnitTest {

  @Test
  public void test() {

    int[] ints =  {0, 5, 10};
    String s = "0,5,10";
    checkEquals(ints, StringUtils.getIndices(s));
    s = "0, 5, 10";
    checkEquals(ints, StringUtils.getIndices(s));
    s = " 0 , 5 , 10 ";
    checkEquals(ints, StringUtils.getIndices(s));

    ints = new int[] {3, 4, 5, 6, 7, 15, 16, 17};
    s = "3-7,15-17";
    checkEquals(ints, StringUtils.getIndices(s));
    s = "3-5,6,7,15,16-17";
    checkEquals(ints, StringUtils.getIndices(s));
    s = "3-5, 6, 7, 15, 16 - 17 ";
    checkEquals(ints, StringUtils.getIndices(s));

    ints = new int[] {3, 4, 5, 6, 7, 15, 14, 13};
    s = "3-7,15-13";
    checkEquals(ints, StringUtils.getIndices(s));
    s = "3 - 7, 15 - 13";
    checkEquals(ints, StringUtils.getIndices(s));
  }

  private void checkEquals(final int[] ints, final int[] indices) {
    Assert.assertTrue(ints.length == indices.length);
    for (int i = 0; i < ints.length; i++) {
       Assert.assertTrue(ints[i] == indices[i]);
    }
  }
}
