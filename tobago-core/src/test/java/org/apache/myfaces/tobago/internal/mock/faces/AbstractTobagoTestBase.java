package org.apache.myfaces.tobago.internal.mock.faces;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
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

import org.apache.myfaces.test.mock.MockApplication;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockFacesContextFactory;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.apache.myfaces.test.mock.MockHttpSession;
import org.apache.myfaces.test.mock.MockRenderKit;
import org.apache.myfaces.test.mock.MockServletConfig;
import org.apache.myfaces.test.mock.MockServletContext;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory;
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Theme;
import org.junit.After;
import org.junit.Before;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.
 * </p>
 * <p>
 * This is a port of the class AbstractJsfTestCase from myfaces-test12 to JUnit 4.
 * It also contains Tobago specifics.
 * </p>
 */

public abstract class AbstractTobagoTestBase {

  // Mock object instances for our tests
  protected MockApplication application = null;
  protected MockServletConfig config = null;
  protected MockExternalContext externalContext = null;
  protected MockFacesContext facesContext = null;
  protected MockFacesContextFactory facesContextFactory = null;
  protected MockLifecycle lifecycle = null;
  protected MockLifecycleFactory lifecycleFactory = null;
  protected MockRenderKit renderKit = null;
  protected MockHttpServletRequest request = null;
  protected MockHttpServletResponse response = null;
  protected MockServletContext servletContext = null;
  protected MockHttpSession session = null;

  // Thread context class loader saved and restored after each test
  private ClassLoader threadContextClassLoader = null;


  /**
   * <p>Set up instance variables required by this test case.</p>
   */
  @Before
  public void setUp() throws Exception {

    // Set up a new thread context class loader
    threadContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0],
        this.getClass().getClassLoader()));

    // Set up Servlet API Objects
    servletContext = new MockServletContext();
    config = new MockServletConfig(servletContext);
    session = new MockHttpSession();
    session.setServletContext(servletContext);
    request = new MockHttpServletRequest(session);
    request.setServletContext(servletContext);
    response = new MockHttpServletResponse();

    // Set up JSF API Objects
    FactoryFinder.releaseFactories();
    FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        "org.apache.myfaces.test.mock.MockApplicationFactory");
    FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
        "org.apache.myfaces.test.mock.MockFacesContextFactory");
    FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
        "org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory");
    FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        "org.apache.myfaces.test.mock.MockRenderKitFactory");

    externalContext =
        new MockExternalContext(servletContext, request, response);
    lifecycleFactory = (MockLifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    lifecycle = (MockLifecycle)
        lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    facesContextFactory = (MockFacesContextFactory)
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    facesContext = (MockFacesContext)
        facesContextFactory.getFacesContext(servletContext,
            request,
            response,
            lifecycle);
    externalContext = (MockExternalContext) facesContext.getExternalContext();
    UIViewRoot root = new UIViewRoot();
    root.setViewId("/viewId");
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    facesContext.setViewRoot(root);
    ApplicationFactory applicationFactory = (ApplicationFactory)
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    application = (MockApplication) applicationFactory.getApplication();
    facesContext.setApplication(application);
    RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    renderKit = new MockRenderKit();
    renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);

    // Tobago specific extensions

    TobagoConfig tobagoConfig = new TobagoConfig();
    Theme theme = new MockTheme("default", "Default Mock Theme", Collections.EMPTY_LIST);
    Theme one = new MockTheme("one", "Mock Theme One", Arrays.asList(theme));
    Map<String, Theme> availableThemes = new HashMap<String, Theme>();
    availableThemes.put(theme.getName(), theme);
    availableThemes.put(one.getName(), one);
    tobagoConfig.setAvailableThemes(availableThemes);
    tobagoConfig.resolveThemes();
    tobagoConfig.initProjectState(servletContext);
    servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);

    final ClientProperties clientProperties = new ClientProperties();
    clientProperties.setTheme(one);
    session.setAttribute(ClientProperties.MANAGED_BEAN_NAME, clientProperties);

    // XXX is there a better way? Get it from Tobagos generated faces-config.xml?
    application.addComponent(ComponentTypes.IN, UIInput.class.getName());
    application.addComponent(ComponentTypes.OUT, UIOut.class.getName());
    application.addComponent("javax.faces.ViewRoot", "org.apache.myfaces.tobago.component.UIViewRoot");
    application.addComponent("javax.faces.Command", "javax.faces.component.UICommand");
    application.addComponent("org.apache.myfaces.tobago.Command", "org.apache.myfaces.tobago.component.UICommand");
    application.addComponent("org.apache.myfaces.tobago.Link", "org.apache.myfaces.tobago.component.UILink");
    application.addComponent("org.apache.myfaces.tobago.Button", "org.apache.myfaces.tobago.component.UIButton");

  }

  /**
   * <p>Tear down instance variables required by this test case.</p>
   */
  @After
  public void tearDown() throws Exception {

    application = null;
    config = null;
    externalContext = null;
    facesContext.release();
    facesContext = null;
    lifecycle = null;
    lifecycleFactory = null;
    renderKit = null;
    request = null;
    response = null;
    servletContext = null;
    session = null;
    FactoryFinder.releaseFactories();

    Thread.currentThread().setContextClassLoader(threadContextClassLoader);
    threadContextClassLoader = null;

  }
}
