package org.apache.myfaces.tobago.taglib.component;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.RenderersConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.taglib.TobagoTag;
import org.apache.myfaces.tobago.mock.faces.MockExternalContext;
import org.apache.myfaces.tobago.mock.faces.MockFacesContext;
import org.apache.myfaces.tobago.mock.faces.MockRenderKit;
import org.apache.myfaces.tobago.mock.servlet.MockHttpServletRequest;
import org.apache.myfaces.tobago.mock.servlet.MockHttpServletResponse;
import org.apache.myfaces.tobago.mock.servlet.MockServletContext;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenericComponentTagUnitTest extends GenericTestBase {

  private static final Logger LOG = LoggerFactory.getLogger(GenericComponentTagUnitTest.class);

  private Application application;
  private MockFacesContext facesContext;
  //private PageContext pageContext;


  public GenericComponentTagUnitTest(String name) {
    super(name);

  }

  public void setUp() throws Exception {
    String[] tldPaths = new String[1];
    tldPaths[0] = "META-INF/org/apache/myfaces/tobago/taglib/component/tobago.tld";
    setTldPaths(tldPaths);
    super.setUp();

    MockServletContext servletContext = new MockServletContext();
    TobagoConfig tobagoConfig = new TobagoConfig();
    Theme theme = new MockTheme("default", Collections.EMPTY_LIST);
    Theme theme1 = new MockTheme("one", Arrays.asList(theme));
    Map<String, Theme> availableThemes = new HashMap<String, Theme>();
    availableThemes.put(theme.getName(), theme);
    availableThemes.put(theme1.getName(), theme1);
    tobagoConfig.setAvailableThemes(availableThemes);
    tobagoConfig.resolveThemes();
    tobagoConfig.initProjectState(servletContext);
    servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    //pageContext = new MockPageContext(request);
    ExternalContext externalContext =
        new MockExternalContext(servletContext, request, response);
    Lifecycle lifecycle = null;
    facesContext = new MockFacesContext(externalContext, lifecycle);
    // Set up Faces API Objects
    FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        "org.apache.myfaces.tobago.mock.faces.MockApplicationFactory");
    FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        "org.apache.myfaces.tobago.mock.faces.MockRenderKitFactory");

    ApplicationFactory applicationFactory = (ApplicationFactory)
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    application = applicationFactory.getApplication();
    application.addComponent("javax.faces.ViewRoot", "org.apache.myfaces.tobago.component.UIViewRoot");
    application.addComponent("javax.faces.Command", "javax.faces.component.UICommand");
    application.addComponent("org.apache.myfaces.tobago.Command", "org.apache.myfaces.tobago.component.UICommand");
    application
        .addComponent("org.apache.myfaces.tobago.LinkCommand", "org.apache.myfaces.tobago.component.UILink");
    application
        .addComponent("org.apache.myfaces.tobago.ButtonCommand", "org.apache.myfaces.tobago.component.UIButton");

    facesContext.setApplication(application);
    UIViewRoot root = facesContext
        .getApplication().getViewHandler().createView(facesContext, "testViewId");
    root.setViewId("/viewId");
    facesContext.setViewRoot(root);

    RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = new MockRenderKit();
    try {
      renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);
    } catch (IllegalArgumentException e) {
      // ignore
    }
    request.setAttribute("peter", new DummyBean(true, 20, "Peter"));
    request.setAttribute("marry", new DummyBean(false, 17, "Marry"));
  }

  public void testComponent()
      throws JspException, IllegalAccessException, InstantiationException, ClassNotFoundException {
    // TODO create new test
    /*for (Tld tld : tlds) {
      for (net.sf.maventaglib.checker.Tag tag : tld.getTags()) {
        javax.servlet.jsp.tagext.Tag tagInstance = getTagInstance(tag);
        if (tagInstance instanceof UIComponentTag
            && (tagInstance instanceof ButtonTag || tagInstance instanceof LinkTag)) {
          LOG.info("testing tag: " + tagInstance.getClass().getName());
          testComponent(tagInstance);
        }
      }
    }*/
  }

  private void testComponent(Tag tag) throws JspException {
    if (tag instanceof TobagoTag) {
      TobagoTag tobagoTag = (TobagoTag) tag;
      //MockViewTag root = new MockViewTag();
      //root.setPageContext(pageContext);
      //root.setRendered("false");
      //root.doStartTag();
      //tobagoTag.setParent(root);
      //tobagoTag.setPageContext(pageContext);
//      tobagoTag.setRendered("true");
      //tobagoTag.setDisabled("#{peter.male}");
      //tobagoTag.setHeight("#{marry.size}");
      tobagoTag.doStartTag();
      UIComponent component = tobagoTag.getComponentInstance();
      UICommand command = (UICommand) component;
      Object disabled = component.getAttributes().get(Attributes.DISABLED);
      LOG.debug("disabled = '" + disabled + "'");
      Map attributes = component.getAttributes();
      for (Iterator i = attributes.keySet().iterator(); i.hasNext();) {
        Object value = i.next();
        LOG.debug("value = " + value);
      }
      Object height = attributes.get(Attributes.HEIGHT);
      LOG.debug("height = '" + height + "'");

      assertTrue(ComponentUtils.getBooleanAttribute(command, Attributes.DISABLED));
      assertFalse(ComponentUtils.getBooleanAttribute(command, Attributes.HEIGHT));
    }
  }

  private static class MockTheme implements Theme {
    private String name;
    private List<Theme> fallbackThemeList;

    private MockTheme(String name, List<Theme> fallbackThemeList) {
      this.name = name;
      this.fallbackThemeList = fallbackThemeList;
    }

    public String getName() {
      return name;
    }

    public List<Theme> getFallbackList() {
      return fallbackThemeList;
    }

    public String getDisplayName() {
      return "";
    }

    public String getResourcePath() {
      return null;
    }

    public RenderersConfig getRenderersConfig() {
      return null;
    }
  }
}

