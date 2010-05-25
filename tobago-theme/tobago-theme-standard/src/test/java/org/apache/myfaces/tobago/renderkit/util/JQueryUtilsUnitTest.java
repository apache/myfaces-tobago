package org.apache.myfaces.tobago.renderkit.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Assert;
import org.junit.Test;

public class JQueryUtilsUnitTest {

  @Test
  public void testEscapeId() {
    Assert.assertEquals("test", JQueryUtils.escapeId("test"));
    Assert.assertEquals("\\\\:t\\\\.e-s\\\\.t\\\\:", JQueryUtils.escapeId(":t.e-s.t:"));
  }

  @Test
  public void testEscapeValue() {
    Assert.assertEquals("te\\\\st", JQueryUtils.escapeValue("te\\st"));
  }
  
  @Test
  public void testEscapeSelector() {
    Assert.assertEquals("test", JQueryUtils.escapeSelector("test"));
    Assert.assertEquals("\\\\:t\\\\.e-s\\\\.t\\\\:", JQueryUtils.escapeSelector(":t.e-s.t:"));
    Assert.assertEquals("\\\\#", JQueryUtils.escapeSelector("#"));
    Assert.assertEquals("\\\\;", JQueryUtils.escapeSelector(";"));
    Assert.assertEquals("\\\\&", JQueryUtils.escapeSelector("&"));
    Assert.assertEquals("\\\\,", JQueryUtils.escapeSelector(","));
    Assert.assertEquals("\\\\.", JQueryUtils.escapeSelector("."));
    Assert.assertEquals("\\\\+", JQueryUtils.escapeSelector("+"));
    Assert.assertEquals("\\\\*", JQueryUtils.escapeSelector("*"));
    Assert.assertEquals("\\\\~", JQueryUtils.escapeSelector("~"));
    Assert.assertEquals("\\\\'", JQueryUtils.escapeSelector("'"));
    Assert.assertEquals("\\\\:", JQueryUtils.escapeSelector(":"));
    Assert.assertEquals("\\\\\\\\", JQueryUtils.escapeSelector("\\"));
    Assert.assertEquals("\\\\\"", JQueryUtils.escapeSelector("\""));
    Assert.assertEquals("\\\\!", JQueryUtils.escapeSelector("!"));
    Assert.assertEquals("\\\\^", JQueryUtils.escapeSelector("^"));
    Assert.assertEquals("\\\\$", JQueryUtils.escapeSelector("$"));
    Assert.assertEquals("\\\\[", JQueryUtils.escapeSelector("["));
    Assert.assertEquals("\\\\]", JQueryUtils.escapeSelector("]"));
    Assert.assertEquals("\\\\(", JQueryUtils.escapeSelector("("));
    Assert.assertEquals("\\\\)", JQueryUtils.escapeSelector(")"));
    Assert.assertEquals("\\\\=", JQueryUtils.escapeSelector("="));
    Assert.assertEquals("\\\\>", JQueryUtils.escapeSelector(">"));
    Assert.assertEquals("\\\\|", JQueryUtils.escapeSelector("|"));
    Assert.assertEquals("\\\\/", JQueryUtils.escapeSelector("/"));
  }

  @Test
  public void selectId() {
    Assert.assertEquals("jQuery('#test\\\\:test')", JQueryUtils.selectId("test:test"));
  }
}
