package org.apache.myfaces.tobago.util;

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

import java.util.Locale;

public class LocaleUtilsUnitTest {

  private static final Locale D1 = new Locale("de");
  private static final Locale D2 = new Locale("de", "DE");
  private static final Locale D3 = new Locale("de", "DE", "NS");

  private static final String S1 = "de";
  private static final String S2 = "de_DE";
  private static final String S3 = "de_DE_NS";

  private static final String D = "default";

  @Test
  public void testCreateLocale() {
    Assert.assertEquals(D1, LocaleUtils.createLocale(S1));
    Assert.assertEquals(D2, LocaleUtils.createLocale(S2));
    Assert.assertEquals(D3, LocaleUtils.createLocale(S3));
  }

  @Test
  public void testLocaleList() {
    Assert.assertArrayEquals(new Locale[]{D1}, LocaleUtils.getLocaleList(D1).toArray());
    Assert.assertArrayEquals(new Locale[]{D2, D1}, LocaleUtils.getLocaleList(D2).toArray());
    Assert.assertArrayEquals(new Locale[]{D3, D2, D1}, LocaleUtils.getLocaleList(D3).toArray());
  }

  @Test
  public void testLocaleSuffixList() {
    Assert.assertArrayEquals(new String[]{'_' + S1, ""}, LocaleUtils.getLocaleSuffixList(D1).toArray());
    Assert.assertArrayEquals(new String[]{'_' + S2, '_' + S1, ""}, LocaleUtils.getLocaleSuffixList(D2).toArray());
    Assert.assertArrayEquals(new String[]{'_' + S3, '_' + S2, '_' + S1, ""},
        LocaleUtils.getLocaleSuffixList(D3).toArray());
  }
}
