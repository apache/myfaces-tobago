/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:21:55.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.mock.faces.MockExternalContext;
import com.atanion.mock.faces.MockFacesContext;
import com.atanion.mock.faces.MockRenderKit;
import com.atanion.mock.faces.MockViewTag;
import com.atanion.mock.servlet.MockHttpServletRequest;
import com.atanion.mock.servlet.MockHttpServletResponse;
import com.atanion.mock.servlet.MockPageContext;
import com.atanion.mock.servlet.MockServletContext;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.Iterator;
import java.util.Map;

public class GenericComponentTagUnitTest extends GenericTestBase {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(GenericComponentTagUnitTest.class);

// ----------------------------------------------------------------- attributes

  private Application application;
  private MockFacesContext facesContext;
  private PageContext pageContext;

// --------------------------------------------------------------- constructors

  public GenericComponentTagUnitTest(String name) {
    super(name);
  }

// ----------------------------------------------------------- business methods

  public void setUp() throws Exception {
    super.setUp();

    ServletContext servletContext = new MockServletContext();
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    pageContext = new MockPageContext(request);
    ExternalContext externalContext =
        new MockExternalContext(servletContext, request, response);
    Lifecycle lifecycle = null;
    facesContext = new MockFacesContext(externalContext, lifecycle);
    // Set up Faces API Objects
    FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        "com.atanion.mock.faces.MockApplicationFactory");
    FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
           "com.atanion.mock.faces.MockRenderKitFactory");

    ApplicationFactory applicationFactory = (ApplicationFactory)
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    application = applicationFactory.getApplication();
    application.addComponent("javax.faces.Command", "javax.faces.component.UICommand");

    facesContext.setApplication(application);
    UIViewRoot root = facesContext
        .getApplication().getViewHandler().createView(facesContext, null);
    root.setViewId("/viewId");
    facesContext.setViewRoot(root);

    RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = new MockRenderKit();
    try {
        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                                      renderKit);
    } catch (IllegalArgumentException e) {
        ;
    }
    request.setAttribute("peter", new TestBean(true, 20, "Peter"));
    request.setAttribute("marry", new TestBean(false, 17, "Marry"));
  }

  public void testComponent() throws JspException {
//    for (int i = 0; i < componentTagList.length; i++) { todo
    for (int i = 0; i < 1; i++) {
      UIComponentTag tag = componentTagList[i];
      LOG.info("testing tag: " + tag.getClass().getName());
      testComponent(tag);
    }
  }

  private void testComponent(Tag tag) throws JspException {
    if (tag instanceof TobagoTag) {
      TobagoTag tobagoTag = (TobagoTag) tag;
      MockViewTag root = new MockViewTag();
      root.setPageContext(pageContext);
      root.setRendered("false");
      root.doStartTag();
      tobagoTag.setParent(root);
      tobagoTag.setPageContext(pageContext);
//      tobagoTag.setRendered("true");
      tobagoTag.setDisabled("#{peter.male}");
      tobagoTag.setHeight("#{marry.size}");
      tobagoTag.doStartTag();
      UIComponent component = tobagoTag.getComponentInstance();
      UICommand command = (UICommand) component;
      Object disabled = component.getAttributes().get(TobagoConstants.ATTR_DISABLED);
      LOG.debug("disabled = '" + disabled + "'");
      Map attributes = component.getAttributes();
      for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ){
        Object value = i.next();
        LOG.debug("value = "  + value);
      }
      Object height = attributes.get(TobagoConstants.ATTR_HEIGHT);
      LOG.debug("height = '" + height + "'");

      assertTrue(ComponentUtil.getBooleanAttribute(command, TobagoConstants.ATTR_DISABLED));
      assertFalse(ComponentUtil.getBooleanAttribute(command, TobagoConstants.ATTR_HEIGHT));
    }
  }
}

