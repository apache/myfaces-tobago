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

import junit.framework.Assert;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TobagoConfigParserUnitTest {

  @Test
  public void testParser() throws Exception {
    generalTest("tobago-config-2.0.xml");
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

    Assert.assertEquals("my-name", fragment.getName());

    Assert.assertEquals(1, fragment.getAfter().size());
    Assert.assertEquals("my-after", fragment.getAfter().get(0));

    Assert.assertEquals(2, fragment.getBefore().size());
    Assert.assertEquals("my-before-1", fragment.getBefore().get(0));
    Assert.assertEquals("my-before-2", fragment.getBefore().get(1));

    Assert.assertEquals(2, fragment.getResourceDirs().size());
    Assert.assertEquals("my-resource-1", fragment.getResourceDirs().get(0));
    Assert.assertEquals("my-resource-2", fragment.getResourceDirs().get(1));

    Assert.assertEquals(false, fragment.getCreateSessionSecret().booleanValue());
    Assert.assertEquals(false, fragment.getCheckSessionSecret().booleanValue());
    Assert.assertEquals(false, fragment.getPreventFrameAttacks().booleanValue());

    Assert.assertEquals(2, fragment.getContentSecurityPolicy().getDirectiveList().size());
    Assert.assertEquals("default-src 'self'", fragment.getContentSecurityPolicy().getDirectiveList().get(0));
    Assert.assertEquals("frame-src http://apache.org", fragment.getContentSecurityPolicy().getDirectiveList().get(1));

    Assert.assertEquals(2, fragment.getRenderersConfig().getRendererConfigs().size());
    Assert.assertTrue(fragment.getRenderersConfig().isMarkupSupported("myRenderer-1", "my-markup-1"));
    Assert.assertTrue(fragment.getRenderersConfig().isMarkupSupported("myRenderer-2", "my-markup-2-1"));
    Assert.assertTrue(fragment.getRenderersConfig().isMarkupSupported("myRenderer-2", "my-markup-2-2"));

    Assert.assertEquals(2, fragment.getThemeDefinitions().size());
    final ThemeImpl theme1 = fragment.getThemeDefinitions().get(0);
    Assert.assertEquals("my-theme-1", theme1.getName());
    Assert.assertEquals("My Theme 1", theme1.getDisplayName());
    Assert.assertEquals("/my/path-1", theme1.getResourcePath());
    Assert.assertTrue(theme1.isVersioned());
    Assert.assertTrue(theme1.getRenderersConfig().isMarkupSupported("themeRenderer", "theme-markup"));
    Assert.assertTrue(theme1.getProductionResources().isProduction());
    Assert.assertEquals("script.js", theme1.getProductionResources().getScriptList().get(0).getName());
    Assert.assertEquals("style.css", theme1.getProductionResources().getStyleList().get(0).getName());

    final ThemeImpl theme2 = fragment.getThemeDefinitions().get(1);
    Assert.assertEquals("my-theme-2", theme2.getName());
    Assert.assertEquals("my-theme-1", theme2.getFallbackName());
    Assert.assertEquals("/my/path-2", theme2.getResourcePath());
    Assert.assertFalse(theme2.isVersioned());
    Assert.assertFalse(theme2.getResources().isProduction());
    Assert.assertEquals(0, theme2.getResources().getScriptList().size());
    Assert.assertEquals(0, theme2.getResources().getStyleList().size());
    Assert.assertEquals(0, theme2.getProductionResources().getScriptList().size());
    Assert.assertEquals(0, theme2.getProductionResources().getStyleList().size());

    Assert.assertFalse("set-nosniff-header", fragment.getSetNosniffHeader());
  }

  @Test
  public void testParserFor10() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-1.0.30.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assert.assertEquals("speyside", fragment.getDefaultThemeName());
  }

  @Test
  public void testFailParserFor10() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-fail-1.0.30.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    try {
      parser.parse(url);
      Assert.fail("No SAXParseException thrown!");
    } catch (final SAXException e) {
      // okay
    }
  }

  @Test
  public void testParserFor15() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-1.5.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    final TobagoConfigFragment fragment = parser.parse(url);
    Assert.assertEquals("speyside", fragment.getDefaultThemeName());
  }

  @Test
  public void testFailParserFor15() throws Exception {
    final URL url = getClass().getClassLoader().getResource("tobago-config-fail-1.5.xml");
    final TobagoConfigParser parser = new TobagoConfigParser();
    try {
      parser.parse(url);
      Assert.fail("No SAXParseException thrown!");
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
      Assert.fail("No SAXParseException thrown!");
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
      Assert.fail("No SAXParseException thrown!");
    } catch (final SAXException e) {
      // okay
    }
  }

  @Test
  public void testUniqueness() throws IllegalAccessException {
    final Field[] all = TobagoConfigParser.class.getFields();
    final List<Field> fields = new ArrayList<Field>();
    for (final Field field : all) {
      if (field.getType().equals(Integer.TYPE)) {
        fields.add(field);
      }
    }
    // uniqueness
    final TobagoConfigParser dummy = new TobagoConfigParser();
    final Set<Integer> hashCodes = new HashSet<Integer>();
    for (final Field field : fields) {
      hashCodes.add(field.getInt(dummy));
    }
    Assert.assertEquals("All used hash codes must be unique", fields.size(), hashCodes.size());

    // check hash code values
    for (final Field field : fields) {
      final int hash = field.getInt(dummy);
      final String name = field.getName().toLowerCase().replace('_', '-');
      Assert.assertEquals("Are the constants correct?", name.hashCode(), hash);
    }
  }

}
