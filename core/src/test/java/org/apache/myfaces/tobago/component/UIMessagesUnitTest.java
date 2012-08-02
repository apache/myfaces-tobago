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

package org.apache.myfaces.tobago.component;

import junit.framework.TestCase;
import org.apache.myfaces.tobago.mock.faces.MockFacesContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author lofwyr (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIMessagesUnitTest extends TestCase {

  private FacesContext facesContext;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    facesContext = new MockFacesContext();
    facesContext.addMessage("id0", new FacesMessage(FacesMessage.SEVERITY_INFO, "test", "a test"));
    facesContext.addMessage("id0", new FacesMessage(FacesMessage.SEVERITY_WARN, "test", "a test"));
    facesContext.addMessage("id1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "test", "a test"));
    facesContext.addMessage("id1", new FacesMessage(FacesMessage.SEVERITY_FATAL, "test", "a test"));
    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "test", "a test"));

  }

  public void testCreateMessageListAll() {

    UIMessages component = new UIMessages();
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    assertEquals(5, messages.size());
  }

  public void testCreateMessageListGlobalOnly() {

    UIMessages component = new UIMessages();
    component.setGlobalOnly(true);
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    assertEquals(1, messages.size());
  }

  public void testCreateMessageListForId0() {

    UIMessages component = new UIMessages();
    component.setFor("id0");
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    assertEquals(2, messages.size());
  }

  public void testCreateMessageListInfoToWarn() {

    UIMessages component = new UIMessages();
    component.setMaxSeverity(FacesMessage.SEVERITY_WARN);
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    assertEquals(2, messages.size());
  }

  public void testCreateMessageListWarnToError() {

    UIMessages component = new UIMessages();
    component.setMinSeverity(FacesMessage.SEVERITY_WARN);
    component.setMaxSeverity(FacesMessage.SEVERITY_ERROR);
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    assertEquals(2, messages.size());
  }

  public void testCreateMessageListErrorToFatal() {

    UIMessages component = new UIMessages();
    component.setMinSeverity(FacesMessage.SEVERITY_ERROR);
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    assertEquals(3, messages.size());
  }

  public void testCreateMessageListMaxNumber() {

    UIMessages component = new UIMessages();

    component.setMaxNumber(3);
    List<UIMessages.Item> messages = component.createMessageList(facesContext);
    assertEquals(3, messages.size());

    component.setMaxNumber(30000);
    messages = component.createMessageList(facesContext);
    assertEquals(5, messages.size());
  }

  public void testCreateMessageListOrderBySeverity() {

    UIMessages component = new UIMessages();
    component.setOrderBy(UIMessages.OrderBy.SEVERITY);
    List<UIMessages.Item> messages = component.createMessageList(facesContext);

    int mustShrink = FacesMessage.SEVERITY_FATAL.getOrdinal();
    for (UIMessages.Item message : messages) {
      int newValue = message.getFacesMessage().getSeverity().getOrdinal();
      assertTrue(mustShrink >= newValue);
      mustShrink = newValue;
    }
  }

  public void testOrderByEnum() {
    assertEquals(2, UIMessages.OrderBy.values().length);
    assertEquals(UIMessages.OrderBy.OCCURENCE, UIMessages.OrderBy.parse(UIMessages.OrderBy.OCCURENCE_STRING));
    assertEquals(UIMessages.OrderBy.OCCURENCE, UIMessages.OrderBy.parse(UIMessages.OrderBy.OCCURRENCE_STRING));
    assertEquals(UIMessages.OrderBy.SEVERITY, UIMessages.OrderBy.parse(UIMessages.OrderBy.SEVERITY_STRING));
  }

}
