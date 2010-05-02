package org.apache.myfaces.tobago.extension.fix.message;

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

import org.apache.shale.test.base.AbstractJsfTestCase;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Iterator;

/*
 * Date: Aug 17, 2007
 * Time: 8:22:00 AM
 */
public class MessageFixFacesContextUnitTest extends AbstractJsfTestCase {

  public MessageFixFacesContextUnitTest(String name) {
    super(name);
  }

  public void testFacesContext() {
    String[] clientIds = {"_id1", "_id2", "_id3", null};
    FacesMessage[] messages =
        {new FacesMessage(),
            new FacesMessage("Test1"),
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Test2a", "Test2a Detail"),
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Test2b", "Test2b Detail"),
            new FacesMessage("Global Message")};

    FacesContext facesContext = new MessageFixFacesContext(this.facesContext);
    facesContext.addMessage(clientIds[0], messages[0]);
    facesContext.addMessage(clientIds[1], messages[1]);
    facesContext.addMessage(clientIds[2], messages[2]);
    facesContext.addMessage(clientIds[2], messages[3]);
    facesContext.addMessage(clientIds[3], messages[4]);

    Iterator clientIdWithMessages = facesContext.getClientIdsWithMessages();
    int index = 0;
    while (clientIdWithMessages.hasNext()) {
      String clientId = (String) clientIdWithMessages.next();
      assertEquals(clientIds[index], clientId);
      index++;
    }

    Iterator facesMessages = facesContext.getMessages();
    index = 0;
    while (facesMessages.hasNext()) {
      FacesMessage facesMessage = (FacesMessage) facesMessages.next();
      assertEquals(facesMessage, messages[index]);
      index++;
    }
    index = 2;
    facesMessages = facesContext.getMessages(clientIds[index]);
    while (facesMessages.hasNext()) {
      FacesMessage facesMessage = (FacesMessage) facesMessages.next();
      assertEquals(facesMessage, messages[index]);
      index++;
    }

    assertEquals(FacesMessage.SEVERITY_WARN, facesContext.getMaximumSeverity());

  }
}
