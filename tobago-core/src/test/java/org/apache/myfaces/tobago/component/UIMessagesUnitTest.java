package org.apache.myfaces.tobago.component;

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

import org.apache.myfaces.tobago.internal.mock.faces.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

public class UIMessagesUnitTest extends AbstractTobagoTestBase {

  @Before
  public void setUp() throws Exception {
    super.setUp();
    FacesContext facesContext = getFacesContext();
    facesContext.addMessage("id0", new FacesMessage(FacesMessage.SEVERITY_INFO, "test", "a test"));
    facesContext.addMessage("id0", new FacesMessage(FacesMessage.SEVERITY_WARN, "test", "a test"));
    facesContext.addMessage("id1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "test", "a test"));
    facesContext.addMessage("id1", new FacesMessage(FacesMessage.SEVERITY_FATAL, "test", "a test"));
    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "test", "a test"));
  }

  @Test
  public void testCreateMessageListAll() {

    UIMessages component = new UIMessages();
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(5, messages.size());
  }

  @Test
  public void testCreateMessageListGlobalOnly() {

    UIMessages component = new UIMessages();
    component.setGlobalOnly(true);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(1, messages.size());
  }

  @Test
  public void testCreateMessageListForId0() {

    UIMessages component = new UIMessages();
    component.setFor("id0");
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(2, messages.size());
  }

  @Test
  public void testCreateMessageListInfoToWarn() {

    UIMessages component = new UIMessages();
    component.setMaxSeverity(FacesMessage.SEVERITY_WARN);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(2, messages.size());
  }

  @Test
  public void testCreateMessageListWarnToError() {

    UIMessages component = new UIMessages();
    component.setMinSeverity(FacesMessage.SEVERITY_WARN);
    component.setMaxSeverity(FacesMessage.SEVERITY_ERROR);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(2, messages.size());
  }

  @Test
  public void testCreateMessageListErrorToFatal() {

    UIMessages component = new UIMessages();
    component.setMinSeverity(FacesMessage.SEVERITY_ERROR);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(3, messages.size());
  }

  @Test
  public void testCreateMessageListMaxNumber() {

    UIMessages component = new UIMessages();

    component.setMaxNumber(3);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());
    Assert.assertEquals(3, messages.size());

    component.setMaxNumber(30000);
    messages = component.createMessageList(getFacesContext());
    Assert.assertEquals(5, messages.size());
  }

  @Test
  public void testCreateMessageListOrderBySeverity() {

    UIMessages component = new UIMessages();
    component.setOrderBy(UIMessages.OrderBy.SEVERITY);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    int mustShrink = FacesMessage.SEVERITY_FATAL.getOrdinal();
    for (UIMessages.Item message : messages) {
      int newValue = message.getFacesMessage().getSeverity().getOrdinal();
      Assert.assertTrue(mustShrink >= newValue);
      mustShrink = newValue;
    }
  }

  @Test
  public void testOrderByEnum() {
    Assert.assertEquals(2, UIMessages.OrderBy.values().length);
    Assert.assertEquals(UIMessages.OrderBy.OCCURRENCE, UIMessages.OrderBy.parse(UIMessages.OrderBy.OCCURRENCE_STRING));
    Assert.assertEquals(UIMessages.OrderBy.SEVERITY, UIMessages.OrderBy.parse(UIMessages.OrderBy.SEVERITY_STRING));
  }

}
