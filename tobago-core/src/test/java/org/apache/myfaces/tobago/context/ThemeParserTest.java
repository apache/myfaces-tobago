package org.apache.myfaces.tobago.context;

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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ThemeParserTest {

  @Test
  public void test() throws IOException, SAXException {
    TobagoConfig config = new TobagoConfig();
    ThemeBuilder themeBuilder = new ThemeBuilder(config);
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Enumeration<URL> urls = classLoader.getResources("theme-config.xml");

    ThemeParser parser = new ThemeParser();
    ThemeImpl theme = null;
    if  (urls.hasMoreElements()) {
      URL themeUrl = urls.nextElement();
      theme = parser.parse(themeUrl);
      Assert.assertEquals("test", theme.getName());
      Assert.assertNotNull(theme.getResources());
      Assert.assertNotNull(theme.getProductionResources());
      ThemeResources resources = theme.getResources();
      ThemeResources productionResources = theme.getProductionResources();

      Assert.assertEquals(2, resources.getScriptList().size());
      Assert.assertEquals("script/tobago.js", resources.getScriptList().get(0).getName());
      Assert.assertEquals("script/logging.js", resources.getScriptList().get(1).getName());

      Assert.assertEquals(1, productionResources.getScriptList().size());
      themeBuilder.addTheme(theme);
    } else {
      Assert.fail();
    }

    urls = classLoader.getResources("theme-config2.xml");

    ThemeImpl theme2 = null;
    if (urls.hasMoreElements()) {
      URL themeUrl = urls.nextElement();
      theme2 = parser.parse(themeUrl);
      Assert.assertEquals("test2", theme2.getName());
      Assert.assertNotNull(theme2.getResources());
      Assert.assertEquals(1, theme2.getResources().getScriptList().size());
      Assert.assertEquals(1, theme2.getResources().getStyleList().size());
      themeBuilder.addTheme(theme2);
    } else {
      Assert.fail();
    }

    urls = classLoader.getResources("theme-config3.xml");

    ThemeImpl theme3 = null;
    if (urls.hasMoreElements()) {
      URL themeUrl = urls.nextElement();
      theme3 = parser.parse(themeUrl);
      Assert.assertEquals("test3", theme3.getName());
      Assert.assertNull(theme3.getResources());
      themeBuilder.addTheme(theme3);
    } else {
      Assert.fail();
    }

    urls = classLoader.getResources("theme-config4.xml");

    ThemeImpl theme4 = null;
    if (urls.hasMoreElements()) {
      URL themeUrl = urls.nextElement();
      theme4 = parser.parse(themeUrl);
      Assert.assertEquals("test4", theme4.getName());
      Assert.assertNull(theme4.getResources());
      themeBuilder.addTheme(theme4);
    } else {
      Assert.fail();
    }

    themeBuilder.resolveThemes();
    Assert.assertEquals(2, theme.getResources().getScriptList().size());
    Assert.assertEquals("script/tobago.js", theme.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/logging.js", theme.getResources().getScriptList().get(1).getName());

    Assert.assertNotNull(theme2.getResources());
    Assert.assertEquals(3, theme2.getResources().getScriptList().size());
    Assert.assertEquals(1, theme2.getResources().getStyleList().size());
    Assert.assertEquals("script/tobago.js", theme2.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/logging.js", theme2.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/test.js", theme2.getResources().getScriptList().get(2).getName());

    Assert.assertEquals(3, theme3.getResources().getScriptList().size());
    Assert.assertEquals("script/tobago.js", theme3.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/logging.js", theme3.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/test.js", theme3.getResources().getScriptList().get(2).getName());

    Assert.assertEquals(3, theme4.getResources().getScriptList().size());
    Assert.assertEquals("script/tobago.js", theme4.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/logging.js", theme4.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/test.js", theme4.getResources().getScriptList().get(2).getName());


  }
}
