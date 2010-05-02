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

import junit.framework.TestCase;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;
import net.sf.maventaglib.checker.TldParser;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.mock.faces.MockExternalContext;
import org.apache.myfaces.tobago.mock.faces.MockFacesContext;
import org.apache.myfaces.tobago.mock.servlet.MockHttpServletRequest;
import org.apache.myfaces.tobago.mock.servlet.MockHttpServletResponse;
import org.apache.myfaces.tobago.mock.servlet.MockPageContext;
import org.apache.myfaces.tobago.mock.servlet.MockServletContext;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
//import org.apache.myfaces.tobago.mock.servlet.MockPageContext;

public abstract class GenericTestBase extends TestCase {
  private static final Logger LOG = LoggerFactory.getLogger(GenericTestBase.class);

  private Tld[] tlds;
  private String[] tldPaths;
  private Application application;
  private MockFacesContext facesContext;

  public GenericTestBase(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    tlds = new Tld[tldPaths.length];
    for (int i = 0; i < tldPaths.length; i++) {
      InputStream stream = getClass().getClassLoader().getResourceAsStream(tldPaths[i]);
      tlds[i] = getTld(tldPaths[i], stream);
      stream.close();
    }

    MockServletContext servletContext = new MockServletContext();
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
        .addComponent("org.apache.myfaces.tobago.LinkCommand", "org.apache.myfaces.tobago.component.UILinkCommand");
    application
        .addComponent("org.apache.myfaces.tobago.ButtonCommand", "org.apache.myfaces.tobago.component.UIButtonCommand");

    facesContext.setApplication(application);
    UIViewRoot root = facesContext
        .getApplication().getViewHandler().createView(facesContext, "testViewId");
    root.setViewId("/viewId");
    facesContext.setViewRoot(root);
  }

  public void testRelease() throws IllegalAccessException,
      NoSuchMethodException, InvocationTargetException, IOException,
      SAXException, ClassNotFoundException, InstantiationException {
    for (Tld tld : tlds) {
      for (net.sf.maventaglib.checker.Tag tag : tld.getTags()) {
        Tag tagInstance = getTagInstance(tag);
        checkRelease(tagInstance);
      }
    }
  }

  public void testSetterExist() throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, IOException,
      SAXException, ClassNotFoundException, InstantiationException {

    for (Tld tld : tlds) {
      for (net.sf.maventaglib.checker.Tag tag : tld.getTags()) {
        Tag tagInstance = getTagInstance(tag);
        TagAttribute[] attributes = tag.getAttributes();
        for (TagAttribute attribute : attributes) {
          String name = attribute.getAttributeName();
          checkSetter(tagInstance, name);
        }
      }
    }
  }

  protected Tag getTagInstance(net.sf.maventaglib.checker.Tag tag)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String className = tag.getTagClass();
    Class tagClass = Class.forName(className);
    return (Tag) tagClass.newInstance();
  }

  private void checkSetter(javax.servlet.jsp.tagext.Tag tagObject, String name)
      throws IllegalAccessException, InstantiationException,
      NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
    PropertyDescriptor propertyDescriptor
        = PropertyUtils.getPropertyDescriptor(tagObject, name);
    assertNotNull("setter '" + name + "' of class " + tagObject.getClass().getName() + " has "
        + "property descriptor.", propertyDescriptor);
    //assertNotNull("setter '" + name + "' of class " + tagObject.getClass().getName() + " exists.",
    //    propertyDescriptor.getWriteMethod());
  }

  public void setTlds(Tld[] tlds) {
    this.tlds = tlds;
  }

  public void setTldPaths(String[] tldPaths) {
    this.tldPaths = tldPaths;
  }

  private void checkRelease(javax.servlet.jsp.tagext.Tag tag) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, IOException,
      SAXException {
    tag.setPageContext(new MockPageContext());

    HashMap<String, Object> initialValues = new HashMap<String, Object>();
    PropertyDescriptor[] descriptors =
        PropertyUtils.getPropertyDescriptors(tag);

    // store initial values
    for (PropertyDescriptor descriptor : descriptors) {
      if (isTagProperty(descriptor)) {
        String name = descriptor.getName();
        Object value = PropertyUtils.getSimpleProperty(tag, name);
        initialValues.put(name, value);
      }
    }

    // set new values
    for (PropertyDescriptor descriptor : descriptors) {
      if (isTagProperty(descriptor)) {
        String name = descriptor.getName();
        Class propertyType = descriptor.getPropertyType();
        Object value = null;
        if (propertyType == String.class) {
          value = new String("bla");
        } else if (propertyType == Integer.TYPE) {
          value = new Integer(42);
        } else if (propertyType == Boolean.TYPE) {
          value = Boolean.TRUE;
        } else {
          LOG.debug("Unsupported property type '" + propertyType
              + "' for property '" + name + "'");
        }
        PropertyUtils.setSimpleProperty(tag, name, value);
      }
    }

    tag.release();

    // check released values
    for (PropertyDescriptor descriptor : descriptors) {
      if (isTagProperty(descriptor)) {
        String name = descriptor.getName();
        // XXX: who releases id?
        if (name.equals("id")) {
          continue;
        }
        try {
          Object newValue = PropertyUtils.getSimpleProperty(tag, name);
          Object oldValue = initialValues.get(name);
          String msg = "release of property '" + name + "' for tag '"
              + tag.getClass().getName() + "' failed.";
          assertEquals(msg, oldValue, newValue);
          // XXX: first error stops loop
          // if (newValue != null && !newValue.equals(oldValue)) {
        } catch (NoSuchMethodException e1) {
          LOG.error("", e1);
        }
      }
    }
  }

  private boolean isTagProperty(PropertyDescriptor descriptor) {
    if ("parent".equals(descriptor.getName())) {
      return false;
    } else {
      return descriptor.getReadMethod() != null
          && descriptor.getWriteMethod() != null;
    }
  }

  private Tld getTld(String name, InputStream stream) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    db.setEntityResolver(new Resolver());
    Document doc = db.parse(stream);
    return TldParser.parse(doc, name);
  }

  private static class Resolver implements EntityResolver {
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      InputSource inputSource =
          new InputSource(GenericTestBase.class.getResourceAsStream("/web-jsptaglibrary_1_2.dtd"));
      inputSource.setSystemId(systemId);
      return inputSource;
    }
  }
}

