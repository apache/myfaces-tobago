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

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.mock.faces.MockTheme;
import org.apache.myfaces.tobago.internal.webapp.HtmlResponseWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;

import static org.apache.myfaces.tobago.util.ResourceUtils.TOBAGO_RESOURCE_BUNDLE;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.
 * </p>
 * <p>
 * This is a port of the class AbstractJsfTestCase from myfaces-test12 to JUnit 4.
 * It also contains Tobago specifics.
 * </p>
 */

public abstract class AbstractTobagoTestBase extends AbstractJsfTestCase {

  private StringWriter stringWriter;
  private int last = 0;

  /**
   * <p>Set up instance variables required by Tobago test cases.</p>
   */
  @Override
  @BeforeEach
  public void setUp() throws Exception {

    super.setUp();

    stringWriter = new StringWriter();
    getFacesContext().setResponseWriter(new HtmlResponseWriter(stringWriter, "", StandardCharsets.UTF_8));

    // Tobago specific extensions

    final TobagoConfigImpl tobagoConfig = TobagoConfigMergingUnitTest.loadAndMerge("tobago-config-for-unit-tests.xml");
    final ThemeImpl theme = new MockTheme("default", "Default Mock Theme", Collections.emptyList());
    final ThemeImpl one = new MockTheme("one", "Mock Theme One", Collections.singletonList(theme));
    tobagoConfig.addAvailableTheme(theme);
    tobagoConfig.addAvailableTheme(one);
    tobagoConfig.resolveThemes();
    tobagoConfig.initDefaultValidatorInfo();
    servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);

    final TobagoContext tobagoContext = new TobagoContext();
    tobagoContext.setTheme(one);
    facesContext.getViewRoot().setLocale(Locale.ENGLISH);
    request.setAttribute(TobagoContext.BEAN_NAME, tobagoContext);

    // XXX is there a better way? Get it from Tobagos generated faces-config.xml?
    application.addComponent(Tags.in.componentType(), UIIn.class.getName());
    application.addComponent(Tags.out.componentType(), UIOut.class.getName());
    application.addComponent(Tags.panel.componentType(), UIPanel.class.getName());
    application.addComponent(Tags.link.componentType(), UILink.class.getName());
    application.addComponent(Tags.button.componentType(), UIButton.class.getName());
    application.addComponent(Tags.popup.componentType(), UIPopup.class.getName());
    application.addComponent(Tags.style.componentType(), UIStyle.class.getName());
    application.addComponent(Tags.gridLayout.componentType(), UIGridLayout.class.getName());

    application.setMessageBundle("org.apache.myfaces.tobago.context.TobagoMessageBundle");

    ResourceBundleVarNames.addVarName(TOBAGO_RESOURCE_BUNDLE,
        "org.apache.myfaces.tobago.context.TobagoResourceBundle");

    tobagoConfig.lock();
  }

  @Override
  @AfterEach
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public MockFacesContext getFacesContext() {
    return facesContext;
  }

  public MockHttpServletRequest getRequest() {
    return request;
  }

  public String getLastWritten() throws IOException {
    getFacesContext().getResponseWriter().flush(); // is this needed
    final String full = stringWriter.toString();
    final String result = full.substring(last);
    last = full.length();
    return result;
  }
}
