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

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.tobago.context.ThemeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TobagoConfigParserUnitTest {

  @Test
  public void testParser() throws Exception {
    generalTest("tobago-config-2.0.xml");
    generalTest("tobago-config-4.0.xml");
  }

  @Test
  public void testParserUntidy() throws Exception {
    generalTest("tobago-config-untidy-2.0.xml");
  }

  private void generalTest(final String name)
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
    final URL url = getClass().getClassLoader().getResource(name);
    final TobagoConfigParser parser = new TobagoConfigParser();

    final TobagoConfigFragment fragment = parser.parse(url);

    Assertions.assertEquals("my-name", fragment.getName());

    Assertions.assertEquals(1, fragment.getAfter().size());
    Assertions.assertEquals("my-after", fragment.getAfter().get(0));

    Assertions.assertEquals(2, fragment.getBefore().size());
    Assertions.assertEquals("my-before-1", fragment.getBefore().get(0));
    Assertions.assertEquals("my-before-2", fragment.getBefore().get(1));

    Assertions.assertFalse(fragment.getCreateSessionSecret());
    Assertions.assertFalse(fragment.getCheckSessionSecret());
    Assertions.assertFalse(fragment.getPreventFrameAttacks());

    final Map<String, String> directiveMap = fragment.getContentSecurityPolicy().getDirectiveMap();
    final Set<Map.Entry<String, String>> entries = directiveMap.entrySet();
    Assertions.assertEquals(2, entries.size());
    Assertions.assertEquals("'self'", directiveMap.get("default-src"));
    Assertions.assertEquals("http://apache.org", directiveMap.get("child-src"));

    Assertions.assertEquals(2, fragment.getThemeDefinitions().size());
    final ThemeImpl theme1 = fragment.getThemeDefinitions().get(0);
    Assertions.assertEquals("my-theme-1", theme1.getName());
    Assertions.assertEquals("My Theme 1", theme1.getDisplayName());
    Assertions.assertEquals("script.js", theme1.getProductionResources().getScriptList().get(0).getName());
    Assertions.assertEquals("style.css", theme1.getProductionResources().getStyleList().get(0).getName());

    final ThemeImpl theme2 = fragment.getThemeDefinitions().get(1);
    Assertions.assertEquals("my-theme-2", theme2.getName());
    Assertions.assertEquals("my-theme-1", theme2.getFallbackName());
    Assertions.assertEquals(0, theme2.getDevelopmentResources().getScriptList().size());
    Assertions.assertEquals(0, theme2.getDevelopmentResources().getStyleList().size());
    Assertions.assertEquals(0, theme2.getProductionResources().getScriptList().size());
    Assertions.assertEquals(0, theme2.getProductionResources().getStyleList().size());

    Assertions.assertFalse(fragment.getSetNosniffHeader(), "set-nosniff-header");
  }

  @Test
  public void testParserFor10() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-1.0.30.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assertions.assertEquals("speyside", fragment.getDefaultThemeName());
  }

  @Test
  public void testFailParserFor10() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-fail-1.0.30.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    try {
      parser.parse(url);
      Assertions.fail("No SAXParseException thrown!");
    } catch (final SAXException e) {
      // okay
    }
  }

  @Test
  public void testParserFor15() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-1.5.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assertions.assertEquals("speyside", fragment.getDefaultThemeName());
  }

  @Test
  public void testFailParserFor15() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-fail-1.5.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    try {
      parser.parse(url);
      Assertions.fail("No SAXParseException thrown!");
    } catch (final SAXException e) {
      // okay
    }
  }

  @Test
  public void testFailParserFor20() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-fail-2.0.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    try {
      parser.parse(url);
      Assertions.fail("No SAXParseException thrown!");
    } catch (final SAXException e) {
      // okay
    }
  }

  @Test
  public void testFailParserForUnknownVersion() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-fail-unknown-version.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    try {
      parser.parse(url);
      Assertions.fail("No SAXParseException thrown!");
    } catch (final SAXException e) {
      // okay
    }
  }

  @Test
  public void testUniqueness() throws IllegalAccessException {
    final Field[] all = TobagoConfigParser.class.getFields();
    final List<Field> fields = new ArrayList<>();
    for (final Field field : all) {
      if (field.getType().equals(Integer.TYPE)) {
        fields.add(field);
      }
    }
    // uniqueness
    final TobagoConfigParser dummy = new TobagoConfigParser();
    final Set<Integer> hashCodes = new HashSet<>();
    for (final Field field : fields) {
      hashCodes.add(field.getInt(dummy));
    }
    Assertions.assertEquals(fields.size(), hashCodes.size(), "All used hash codes must be unique");

    // check hash code values
    for (final Field field : fields) {
      final int hash = field.getInt(dummy);
      final String name = field.getName().toLowerCase().replace('_', '-');
      Assertions.assertEquals(name.hashCode(), hash, "Are the constants correct?");
    }
  }

  @Test
  public void testThemeCookieUndefined() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-5.0.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assertions.assertNull(fragment.getThemeCookie());
  }

  @Test
  public void testThemeCookieFalse() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-5.1.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assertions.assertFalse(fragment.getThemeCookie());
  }

  @Test
  public void testThemeSessionUndefined() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-5.0.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assertions.assertNull(fragment.getThemeSession());
  }

  @Test
  public void testThemeSessionTrue() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-5.1.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assertions.assertTrue(fragment.getThemeSession());
  }

}
