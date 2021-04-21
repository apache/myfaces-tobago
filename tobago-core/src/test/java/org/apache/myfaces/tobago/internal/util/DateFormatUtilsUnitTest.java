package org.apache.myfaces.tobago.internal.util;

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

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateFormatUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void date() {
    final String p1 = "yyyy-MM-dd";
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertEquals("yyyy-mm-dd", js1.getDatePattern());
    Assertions.assertNull(js1.getTimePattern());
    Assertions.assertNull(js1.getSeparator());
  }

  @Test
  public void time() {
    final String p1 = "HH:mm";  // hour 0-23 : minute 0-59
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertNull(js1.getDatePattern());
    Assertions.assertEquals("HH:mm", js1.getTimePattern());
    Assertions.assertNull(js1.getSeparator());
  }

  @Test
  public void bothDateTime() {
    final String p1 = "yyyy-MM-dd HH:mm";
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertEquals("yyyy-mm-dd", js1.getDatePattern());
    Assertions.assertEquals("HH:mm", js1.getTimePattern());
    Assertions.assertEquals(" ", js1.getSeparator());
  }

  @Test
  public void bothTimeDate() {
    final String p1 = "HH:mm yyyy-MM-dd";
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertEquals("yyyy-mm-dd", js1.getDatePattern());
    Assertions.assertEquals("HH:mm", js1.getTimePattern());
    Assertions.assertEquals(" ", js1.getSeparator());
  }

  @Test
  public void both1() {
    final String p1 = "HH----yyyy";
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertEquals("yyyy", js1.getDatePattern());
    Assertions.assertEquals("HH", js1.getTimePattern());
    Assertions.assertEquals("----", js1.getSeparator());
  }

  @Test
  public void both2() {
    final String p1 = "::yy--ss::";
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertEquals("::yy", js1.getDatePattern());
    Assertions.assertEquals("ss::", js1.getTimePattern());
    Assertions.assertEquals("--", js1.getSeparator());
  }

  @Test
  public void both3() {
    final String p1 = "::MMmm::";
    final DateFormatUtils.DateTimeJavaScriptPattern js1 = new DateFormatUtils.DateTimeJavaScriptPattern(p1);

    Assertions.assertEquals("::mm", js1.getDatePattern());
    Assertions.assertEquals("mm::", js1.getTimePattern());
    Assertions.assertEquals("", js1.getSeparator());
  }
}
