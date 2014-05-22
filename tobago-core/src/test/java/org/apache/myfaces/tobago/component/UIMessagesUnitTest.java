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

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
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
    final FacesContext facesContext = getFacesContext();
    facesContext.addMessage("id0", new FacesMessage(FacesMessage.SEVERITY_INFO, "test", "a test"));
    facesContext.addMessage("id0", new FacesMessage(FacesMessage.SEVERITY_WARN, "test", "a test"));
    facesContext.addMessage("id1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "test", "a test"));
    facesContext.addMessage("id1", new FacesMessage(FacesMessage.SEVERITY_FATAL, "test", "a test"));
    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "test", "a test"));
  }

  @Test
  public void testCreateMessageListAll() {

    final UIMessages component = new UIMessages();
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(5, messages.size());
  }

  @Test
  public void testCreateMessageListGlobalOnly() {

    final UIMessages component = new UIMessages();
    component.setGlobalOnly(true);
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(1, messages.size());
  }

  @Test
  public void testCreateMessageListForId0() {

    final UIMessages component = new UIMessages();
    component.setFor("id0");
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(2, messages.size());
  }

  @Test
  public void testCreateMessageListInfoToWarn() {

    final UIMessages component = new UIMessages();
    component.setMaxSeverity(FacesMessage.SEVERITY_WARN);
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(2, messages.size());
  }

  @Test
  public void testCreateMessageListWarnToError() {

    final UIMessages component = new UIMessages();
    component.setMinSeverity(FacesMessage.SEVERITY_WARN);
    component.setMaxSeverity(FacesMessage.SEVERITY_ERROR);
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(2, messages.size());
  }

  @Test
  public void testCreateMessageListErrorToFatal() {

    final UIMessages component = new UIMessages();
    component.setMinSeverity(FacesMessage.SEVERITY_ERROR);
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    Assert.assertEquals(3, messages.size());
  }

  @Test
  public void testCreateMessageListMaxNumber() {

    final UIMessages component = new UIMessages();

    component.setMaxNumber(3);
    List<UIMessages.Item> messages = component.createMessageList(getFacesContext());
    Assert.assertEquals(3, messages.size());

    component.setMaxNumber(30000);
    messages = component.createMessageList(getFacesContext());
    Assert.assertEquals(5, messages.size());
  }

  @Test
  public void testCreateMessageListOrderBySeverity() {

    final UIMessages component = new UIMessages();
    component.setOrderBy(UIMessages.OrderBy.SEVERITY);
    final List<UIMessages.Item> messages = component.createMessageList(getFacesContext());

    int mustShrink = FacesMessage.SEVERITY_FATAL.getOrdinal();
    for (final UIMessages.Item message : messages) {
      final int newValue = message.getFacesMessage().getSeverity().getOrdinal();
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
