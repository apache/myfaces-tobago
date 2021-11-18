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

import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.ThemeScript;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TobagoConfigMergingUnitTest {

  @Test
  public void testPreventFrameAttacksCascadingDefault() {

    final TobagoConfig config =
        new TobagoConfig(null, "tobago-config-merge-0.xml", "tobago-config-merge-1.xml");

    Assertions.assertFalse(config.isPreventFrameAttacks());
  }

  @Test
  public void testPreventFrameAttacks() {

    final TobagoConfig config = new TobagoConfig(null, "tobago-config-merge-0.xml");

    Assertions.assertFalse(config.isPreventFrameAttacks());
  }

  @Test
  public void testPreventFrameAttacksDefault() {

    final TobagoConfig config = new TobagoConfig(null, "tobago-config-merge-1.xml");

    Assertions.assertTrue(config.isPreventFrameAttacks());
  }

  @Test
  public void testContentSecurityPolicy() {

    final TobagoConfig config = new TobagoConfig(null, "tobago-config-merge-0.xml");

    Assertions.assertSame(config.getContentSecurityPolicy().getMode(), ContentSecurityPolicy.Mode.ON);
    final Map<String, String> directiveMap = config.getContentSecurityPolicy().getDirectiveMap();
    Assertions.assertEquals(1, directiveMap.size());
    Assertions.assertEquals("'self'", directiveMap.get("default-src"));
  }

  @Test
  public void testContentSecurityPolicyExtend() {

    final TobagoConfig config =
        new TobagoConfig(null, "tobago-config-merge-0.xml", "tobago-config-merge-1.xml");

    Assertions.assertSame(config.getContentSecurityPolicy().getMode(), ContentSecurityPolicy.Mode.REPORT_ONLY);
    final Map<String, String> directiveMap = config.getContentSecurityPolicy().getDirectiveMap();
    Assertions.assertEquals(2, directiveMap.size());
    Assertions.assertEquals("'self'", directiveMap.get("default-src"));
    Assertions.assertEquals("http://apache.org", directiveMap.get("image-src"));
  }

  @Test
  public void testContentSecurityPolicyOff() {

    final TobagoConfig config = new TobagoConfig(null, "tobago-config-merge-0.xml", "tobago-config-merge-1.xml",
        "tobago-config-merge-2.xml");

    Assertions.assertSame(config.getContentSecurityPolicy().getMode(), ContentSecurityPolicy.Mode.OFF);
    Assertions.assertEquals(2, config.getContentSecurityPolicy().getDirectiveMap().size());
  }

  @Test
  public void testContentSecurityPolicyNameAttribute() {

    final TobagoConfig config =
        new TobagoConfig(null, "tobago-config-merge-0.xml", "tobago-config-merge-3.xml");

    Assertions.assertSame(config.getContentSecurityPolicy().getMode(), ContentSecurityPolicy.Mode.ON);
    final Map<String, String> directiveMap = config.getContentSecurityPolicy().getDirectiveMap();
    Assertions.assertEquals(1, directiveMap.size());
    Assertions.assertEquals("'self' https:", directiveMap.get("default-src"));
  }

  @Test
  public void testMimeTypes() {

    final TobagoConfig config = new TobagoConfig(null, "tobago-config-merge-0.xml", "tobago-config-merge-1.xml",
        "tobago-config-merge-2.xml");

    final Map<String, String> mimeTypes = config.getMimeTypes();
    Assertions.assertEquals(3, mimeTypes.size());
    Assertions.assertEquals("test/one", mimeTypes.get("test-1"));
    Assertions.assertEquals("test/zwei", mimeTypes.get("test-2"));
    Assertions.assertEquals("test/three", mimeTypes.get("test-3"));
  }

  @Test
  public void testResourcePriority() {

    final TobagoConfig config =
        new TobagoConfig(null, "tobago-config-5.0.xml", "tobago-config-5.0-replace.xml");

    final String[] expected = new String[]{
        "script-1.js",
        "script-2.js",
        "script-3-replacement.js",
        "script-4.js",
        "script-5.js",
        "script-undefined-a.js",
        "script-undefined-b.js",
        "script-undefined-c.js",
        "script-undefined-d.js",
        "script-undefined-e.js"
    };
    final ThemeScript[] ex = new ThemeScript[expected.length];
    int i = 0;
    for (String script : expected) {
      ex[i] = new ThemeScript();
      ex[i++].setName(script);
    }

//    config.resolveThemes();
    final Theme defaultTheme = config.getDefaultTheme();
    final ThemeScript[] scripts = defaultTheme.getScriptResources(true);

    Assertions.assertArrayEquals(ex, scripts);

    Assertions.assertEquals("some-version-2", defaultTheme.getVersion());
  }

  @Test
  public void testMergeThemePatch() {

    final TobagoConfig config12 =
        new TobagoConfig(null, "tobago-config-theme-merge-1.xml", "tobago-config-theme-merge-2.xml");
    Assertions.assertEquals("2.0", config12.getDefaultTheme().getVersion());

    final ThemeScript[] scripts12 = config12.getDefaultTheme().getScriptResources(false);
    Assertions.assertEquals(2, scripts12.length);
    Assertions.assertEquals("script-1", scripts12[0].getName());
    Assertions.assertEquals("script-2", scripts12[1].getName());

    final TobagoConfig config21 =
        new TobagoConfig(null, "tobago-config-theme-merge-2.xml", "tobago-config-theme-merge-1.xml");
    Assertions.assertEquals("2.0", config21.getDefaultTheme().getVersion());

    final ThemeScript[] scripts21 = config21.getDefaultTheme().getScriptResources(false);
    Assertions.assertEquals(2, scripts21.length);
    Assertions.assertEquals("script-1", scripts21[0].getName());
    Assertions.assertEquals("script-2", scripts21[1].getName());
  }

  @Test
  public void test51() {
    final TobagoConfig config = new TobagoConfig(null, "tobago-config-5.1.xml");

    final Theme theme1 = config.getTheme("my-theme-1");
    Assertions.assertNull(theme1.getTagAttributeDefault(Tags.date, "labelLayout"));
    Assertions.assertEquals("flexLeft", theme1.getTagAttributeDefault(Tags.in, "labelLayout"));
    Assertions.assertEquals("flexLeft", theme1.getTagAttributeDefault(Tags.out, "labelLayout"));

    final Theme theme2 = config.getTheme("my-theme-2");
    Assertions.assertEquals("top", theme2.getTagAttributeDefault(Tags.date, "labelLayout"));
    Assertions.assertEquals("top", theme2.getTagAttributeDefault(Tags.in, "labelLayout"));
    Assertions.assertEquals("flexLeft", theme2.getTagAttributeDefault(Tags.out, "labelLayout"));
  }
}
