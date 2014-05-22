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

package org.apache.myfaces.tobago.internal.context;

import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.context.ThemeResources;
import org.apache.myfaces.tobago.internal.config.ThemeBuilder;
import org.apache.myfaces.tobago.internal.config.TobagoConfigParser;
import org.apache.myfaces.tobago.internal.config.TobagoConfigTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

public class ThemeParserUnitTest {

  @Test
  public void test() throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
    final ThemeBuilder themeBuilder = TobagoConfigTestUtils.createThemeBuilder();
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Enumeration<URL> urls = classLoader.getResources("theme-config.xml");

    final TobagoConfigParser parser = new TobagoConfigParser();
    ThemeImpl theme = null;
    if  (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      theme = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assert.assertEquals("test", theme.getName());
      Assert.assertNotNull(theme.getResources());
      Assert.assertNotNull(theme.getProductionResources());
      final ThemeResources resources = theme.getResources();
      final ThemeResources productionResources = theme.getProductionResources();

      Assert.assertEquals(3, resources.getScriptList().size());
      Assert.assertEquals("script/tobago.js", resources.getScriptList().get(0).getName());
      Assert.assertEquals("script/tobago-logging.js", resources.getScriptList().get(1).getName());
      Assert.assertEquals("script/tobago-console.js", resources.getScriptList().get(2).getName());

      Assert.assertEquals(1, productionResources.getScriptList().size());
      themeBuilder.addTheme(theme);
    } else {
      Assert.fail();
    }

    urls = classLoader.getResources("theme-config2.xml");

    ThemeImpl theme2 = null;
    if (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      theme2 = parser.parse(themeUrl).getThemeDefinitions().get(0);
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
      final URL themeUrl = urls.nextElement();
      theme3 = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assert.assertEquals("test3", theme3.getName());
      Assert.assertEquals(0, theme3.getResources().getScriptList().size());
      Assert.assertEquals(0, theme3.getResources().getStyleList().size());
      themeBuilder.addTheme(theme3);
    } else {
      Assert.fail();
    }

    urls = classLoader.getResources("theme-config4.xml");

    ThemeImpl theme4 = null;
    if (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      theme4 = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assert.assertEquals("test4", theme4.getName());
      Assert.assertEquals(0, theme4.getResources().getScriptList().size());
      Assert.assertEquals(0, theme4.getResources().getStyleList().size());
      themeBuilder.addTheme(theme4);
    } else {
      Assert.fail();
    }

    themeBuilder.resolveThemes();
    Assert.assertEquals(3, theme.getResources().getScriptList().size());
    Assert.assertEquals("script/tobago.js", theme.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/tobago-logging.js", theme.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/tobago-console.js", theme.getResources().getScriptList().get(2).getName());

    Assert.assertNotNull(theme2.getResources());
    Assert.assertEquals(4, theme2.getResources().getScriptList().size());
    Assert.assertEquals(1, theme2.getResources().getStyleList().size());
    Assert.assertEquals("script/tobago.js", theme2.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/tobago-logging.js", theme2.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/tobago-console.js", theme2.getResources().getScriptList().get(2).getName());
    Assert.assertEquals("script/test.js", theme2.getResources().getScriptList().get(3).getName());

    Assert.assertEquals(4, theme3.getResources().getScriptList().size());
    Assert.assertEquals("script/tobago.js", theme3.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/tobago-logging.js", theme3.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/tobago-console.js", theme3.getResources().getScriptList().get(2).getName());
    Assert.assertEquals("script/test.js", theme3.getResources().getScriptList().get(3).getName());

    Assert.assertEquals(4, theme4.getResources().getScriptList().size());
    Assert.assertEquals("script/tobago.js", theme4.getResources().getScriptList().get(0).getName());
    Assert.assertEquals("script/tobago-logging.js", theme4.getResources().getScriptList().get(1).getName());
    Assert.assertEquals("script/tobago-console.js", theme4.getResources().getScriptList().get(2).getName());
    Assert.assertEquals("script/test.js", theme4.getResources().getScriptList().get(3).getName());
  }
}
