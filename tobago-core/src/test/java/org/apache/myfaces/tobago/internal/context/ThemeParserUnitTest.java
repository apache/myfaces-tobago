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
import org.apache.myfaces.tobago.internal.config.TobagoConfigParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

public class ThemeParserUnitTest {

  @Test
  public void test() throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Enumeration<URL> urls = classLoader.getResources("theme-config.xml");

    final TobagoConfigParser parser = new TobagoConfigParser();
    if (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      final ThemeImpl theme = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assertions.assertEquals("test", theme.getName());
      Assertions.assertNotNull(theme.getDevelopmentResources());
      Assertions.assertNotNull(theme.getProductionResources());
      final ThemeResources resources = theme.getDevelopmentResources();
      final ThemeResources productionResources = theme.getProductionResources();

      Assertions.assertEquals(1, resources.getScriptList().size());
      Assertions.assertEquals("script/tobago.js", resources.getScriptList().get(0).getName());
//      Assertions.assertEquals("script/tobago-console.js", resources.getScriptList().get(1).getName());

      Assertions.assertEquals(1, productionResources.getScriptList().size());
    } else {
      Assertions.fail();
    }

    urls = classLoader.getResources("theme-config2.xml");

    if (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      final ThemeImpl theme2 = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assertions.assertEquals("test2", theme2.getName());
      Assertions.assertNotNull(theme2.getDevelopmentResources());
      Assertions.assertEquals(1, theme2.getDevelopmentResources().getScriptList().size());
      Assertions.assertEquals(1, theme2.getDevelopmentResources().getStyleList().size());
    } else {
      Assertions.fail();
    }

    urls = classLoader.getResources("theme-config3.xml");

    if (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      final ThemeImpl theme3 = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assertions.assertEquals("test3", theme3.getName());
      Assertions.assertEquals(0, theme3.getDevelopmentResources().getScriptList().size());
      Assertions.assertEquals(0, theme3.getDevelopmentResources().getStyleList().size());
    } else {
      Assertions.fail();
    }

    urls = classLoader.getResources("theme-config4.xml");

    if (urls.hasMoreElements()) {
      final URL themeUrl = urls.nextElement();
      final ThemeImpl theme4 = parser.parse(themeUrl).getThemeDefinitions().get(0);
      Assertions.assertEquals("test4", theme4.getName());
      Assertions.assertEquals(0, theme4.getDevelopmentResources().getScriptList().size());
      Assertions.assertEquals(0, theme4.getDevelopmentResources().getStyleList().size());
    } else {
      Assertions.fail();
    }
  }
}
