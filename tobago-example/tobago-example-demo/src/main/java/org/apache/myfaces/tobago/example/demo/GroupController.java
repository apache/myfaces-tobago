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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.component.UICommand;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class GroupController implements Serializable {

  private String chatlog;
  private String newMessage;
  private String sendTo;

  public GroupController() {
    chatlog = "Peter: Hi, how are you?";
    newMessage = "I'm fine.";
    sendTo = "SendTo:";
  }

  public String getChatlog() {
    return chatlog;
  }

  public String getNewMessage() {
    return newMessage;
  }

  public void setNewMessage(String newMessage) {
    this.newMessage = newMessage;
  }

  public void sendChat() {
    chatlog += "\nUser Two: " + newMessage;
    newMessage = "";
  }

  public String getSendTo() {
    return sendTo;
  }

  public void setSendTo(String sendTo) {
    this.sendTo = sendTo;
  }

  public void sendToListener(ActionEvent actionEvent) {
    if (actionEvent != null && actionEvent.getComponent() instanceof UICommand) {
      UICommand command = (UICommand) actionEvent.getComponent();
      sendTo = command.getLabel();
    }
  }
}
