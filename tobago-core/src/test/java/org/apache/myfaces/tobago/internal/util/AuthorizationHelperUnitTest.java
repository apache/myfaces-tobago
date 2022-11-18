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

package org.apache.myfaces.tobago.internal.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthorizationHelperUnitTest {

  @Test
  public void testSkipBrackets() {
    Assertions.assertEquals(
        "#{controller.viewDetails}",
        AuthorizationHelper.skipParameterPart("#{controller.viewDetails(cc.attrs.value, facesContext)}"));
    Assertions.assertEquals(
        "#{x}",
        AuthorizationHelper.skipParameterPart("#{x}"));
    Assertions.assertEquals(
        "controller.viewDetails",
        AuthorizationHelper.skipParameterPart("controller.viewDetails(cc.attrs.value, facesContext)"));
    Assertions.assertEquals(
        "x",
        AuthorizationHelper.skipParameterPart("x"));
    Assertions.assertEquals(
        "",
        AuthorizationHelper.skipParameterPart(""));
    Assertions.assertEquals(
        "#{x.a}",
        AuthorizationHelper.skipParameterPart("#{x.a()}"));
    Assertions.assertEquals(
        "#{x.a)(}",
        AuthorizationHelper.skipParameterPart("#{x.a)(}"));
    Assertions.assertEquals(
        "#{x.a)}",
        AuthorizationHelper.skipParameterPart("#{x.a)}"));
    Assertions.assertEquals(
        "#{x.a(}",
        AuthorizationHelper.skipParameterPart("#{x.a(}"));
  }
}
