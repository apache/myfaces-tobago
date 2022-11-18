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

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

// XXX a solution might be putting this example to its own small JAR.

//@ConversationScoped // XXX not supported by Quarkus https://quarkus.io/guides/cdi-reference
@SessionScoped
@Named
public class ConversationController implements Serializable {

  //  @Inject // XXX
  private Conversation conversation;
  private int count = 0;

  public String getConversationStatus() {
    return conversation != null && !conversation.isTransient() ? "started" : "stopped";
  }

  public void beginConversation() {
    conversation.begin();
  }

  public void endConversation() {
    conversation.end();
  }

  public int getCount() {
    return count;
  }

  public void countUp() {
    count++;
  }
}
