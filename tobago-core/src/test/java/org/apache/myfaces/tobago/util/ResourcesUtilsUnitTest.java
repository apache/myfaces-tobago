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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;
import java.util.Locale;
import java.util.MissingResourceException;

import static org.apache.myfaces.tobago.config.TobagoConfig.TOBAGO_CONFIG;

public class ResourcesUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testDefault() {
    Assertions.assertEquals("First Page", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void testGermany() {
    facesContext.getViewRoot().setLocale(Locale.GERMANY);
    Assertions.assertEquals("erste Seite", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void testSpanish() {
    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));
    Assertions.assertEquals("Primera Página", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void testFallback() {
    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("ja"));
    Assertions.assertEquals("First Page", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void test4Bundle() {
    final TobagoConfig tobagoConfig =
        new TobagoConfig((ServletContext) facesContext.getExternalContext().getContext(),
            "theme-config4.xml", "theme-config3.xml", "theme-config2.xml", "theme-config.xml");
    servletContext.setAttribute(TOBAGO_CONFIG, tobagoConfig);
    facesContext.getExternalContext().getApplicationMap().put(TOBAGO_CONFIG, tobagoConfig);
    final TobagoContext tobagoContext = new TobagoContext();
    tobagoContext.setTheme(tobagoConfig.getDefaultTheme());
    request.setAttribute(TobagoContext.BEAN_NAME, tobagoContext);
    ResourceBundleVarNames.addVarName("test4Bundle", "org.apache.myfaces.tobago.theme.test4Bundle");
    ResourceBundleVarNames.addVarName("test3Bundle", "org.apache.myfaces.tobago.theme.test3Bundle");
    ResourceBundleVarNames.addVarName("testBundle", "org.apache.myfaces.tobago.theme.resourcesUtilsTestBundle");

    Assertions.assertEquals("test4", TobagoContext.getInstance(FacesContext.getCurrentInstance()).getTheme().getName());
    Assertions.assertEquals("First Page test4Bundle (default)", ResourceUtils.getString("sheet.first"));

    facesContext.getViewRoot().setLocale(Locale.GERMANY);
    Assertions.assertEquals("erste Seite test4Bundle (DE)", ResourceUtils.getString("sheet.first"));

    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));
    Assertions.assertEquals("First Page test4Bundle (default)", ResourceUtils.getString("sheet.first"));

    facesContext.getViewRoot().setLocale(Locale.ENGLISH);
    Assertions.assertEquals("Last Page testBundle (EN)", ResourceUtils.getString("sheet.last"));

    facesContext.getViewRoot().setLocale(Locale.GERMANY);
    Assertions.assertEquals("letzte Seite testBundle (DE)", ResourceUtils.getString("sheet.last"));

    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));
    Assertions.assertEquals(Locale.US, Locale.getDefault());
    Assertions.assertEquals("Last Page testBundle (EN)", ResourceUtils.getString("sheet.last")); //because of Locale.US

    facesContext.getViewRoot().setLocale(Locale.ENGLISH);
    Assertions.assertEquals("Ascending", ResourceUtils.getString("sheet.ascending"));

    facesContext.getViewRoot().setLocale(Locale.GERMANY);
    Assertions.assertEquals("aufsteigend sortiert resourcesUtilsTestBundle_de",
        ResourceUtils.getString("sheet.ascending"));

    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));
    Assertions.assertEquals("Ascendente resourcesUtilsTestBundle_es", ResourceUtils.getString("sheet.ascending"));
  }

  @Test
  public void testCustomResourceBundle() {
    ResourceBundleVarNames.addVarName("customBundle", "org.apache.myfaces.tobago.theme.test4Bundle");

    facesContext.getViewRoot().setLocale(Locale.ENGLISH);
    Assertions.assertEquals("First Page test4Bundle (default)", ResourceUtils.getString("customBundle", "sheet.first"));

    facesContext.getViewRoot().setLocale(Locale.GERMANY);
    Assertions.assertEquals("erste Seite test4Bundle (DE)", ResourceUtils.getString("customBundle", "sheet.first"));

    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));
    Assertions.assertEquals("First Page test4Bundle (default)", ResourceUtils.getString("customBundle", "sheet.first"));

    facesContext.getViewRoot().setLocale(Locale.ENGLISH);
    Assertions.assertThrows(MissingResourceException.class,
        () -> ResourceUtils.getString("customBundle", "sheet.last"));
  }
}
