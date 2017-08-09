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

package org.apache.myfaces.tobago.apt.processor;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CheckstyleConfigGeneratorUnitTest {

  @Test
  public void testGetRegExpForUndefinedAttributes() {

    String regexp = CheckstyleConfigGenerator.getRegExpForUndefinedAttributes(
        "tc", "textarea", Arrays.asList("attr1", "attr2"));

    Assert.assertEquals(
        "<tc:textarea(\\s+(attr1|attr2|xmlns:\\w*)=\\\"([^\"=<>]*)\\\")*\\s+(?!(attr1|attr2|xmlns:\\w*|\\W))", regexp);

    regexp = ".*" + regexp + ".*";

    Assert.assertFalse("<tc:textarea attr1=\"bla bla\"/>".matches(regexp));
    Assert.assertFalse("<tc:textarea attr2=\"bla bla\"/>".matches(regexp));
    Assert.assertFalse("<tc:textarea attr2=\"bla bla\" attr1=\".....\"/>".matches(regexp));
    Assert.assertTrue("<tc:textarea hallo=\"bla bla\"/>".matches(regexp));

    Assert.assertFalse("<tc:textarea xmlns:f=\"http://java.sun.com/jsf/core\" attr1=\"bla bla\"/>".matches(regexp));
    Assert.assertTrue("<tc:textarea xmlns:f=\"http://java.sun.com/jsf/core\" hallo=\"bla bla\"/>".matches(regexp));

    Assert.assertFalse("<any> <tc:textarea      attr1=\"bla bla\"     /> <any>".matches(regexp));
    Assert.assertFalse("<any> <tc:textarea         attr2=\"bla bla\"  /><any>".matches(regexp));
    Assert.assertFalse("<any> <tc:textarea            attr2=\"bla bla\"    attr1=\".....\"/><any> ".matches(regexp));
    Assert.assertTrue("<any> <tc:textarea        hallo=\"bla bla\"         /> <any> ".matches(regexp));
  }
}
