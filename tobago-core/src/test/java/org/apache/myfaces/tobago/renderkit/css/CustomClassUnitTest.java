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

package org.apache.myfaces.tobago.renderkit.css;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomClassUnitTest {

  @Test
  public void test() {
    Assertions.assertEquals("test", new CustomClass("test").getName(), "simple");
    Assertions.assertEquals("test2", new CustomClass("test2").getName(), "number");
    Assertions.assertEquals("test3 and test4", new CustomClass("test3 and test4").getName(), "list");
    Assertions.assertEquals("test5 test6", new CustomClass("     test5     test6       ").getName(), "space");
    Assertions.assertEquals("test-8", new CustomClass("test-8").getName(), "-");
    Assertions.assertEquals("__t_e_s_t-9 _d", new CustomClass("__t_e_s_t-9 _d").getName(), "_");

    Assertions.assertEquals("", new CustomClass("test.10").getName(), ".");
    Assertions.assertEquals("", new CustomClass("test\t11").getName(), "tab");
    Assertions.assertEquals("", new CustomClass("test:12").getName(), ":");
  }

}
