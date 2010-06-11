package org.apache.myfaces.tobago.internal.taglib.component;

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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.taglib.TobagoTag;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.Iterator;
import java.util.Map;

public class GenericComponentTagUnitTest extends GenericTestBase {

  private static final Logger LOG = LoggerFactory.getLogger(GenericComponentTagUnitTest.class);

  @Before
  public void setUp() throws Exception {
    String[] tldPaths = new String[1];
    tldPaths[0] = "META-INF/org/apache/myfaces/tobago/internal/taglib/component/tobago.tld";
    setTldPaths(tldPaths);
    super.setUp();
    request.setAttribute("peter", new DummyBean(true, 20, "Peter"));
    request.setAttribute("marry", new DummyBean(false, 17, "Marry"));
  }

  @Test
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

      Assert.assertTrue(ComponentUtils.getBooleanAttribute(command, Attributes.DISABLED));
      Assert.assertFalse(ComponentUtils.getBooleanAttribute(command, Attributes.HEIGHT));
    }
  }
}
