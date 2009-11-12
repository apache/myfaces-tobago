package org.apache.myfaces.tobago.apt.generate;

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

/*
 * Date: 13.03.2008
 * Time: 15:27:56
 */
public class ComponentInfo extends TagInfo {
  private boolean invokeOnComponent;
  private boolean messages;

  public ComponentInfo(String sourceClass, String qualifiedName, String rendererType) {
    super(sourceClass, qualifiedName, rendererType);
  }

  public boolean isInvokeOnComponent() {
    return invokeOnComponent;
  }

  public void setInvokeOnComponent(boolean invokeOnComponent) {
    this.invokeOnComponent = invokeOnComponent;
  }

  public boolean isMessages() {
    return messages;
  }

  public void setMessages(boolean messages) {
    this.messages = messages;
  }
}
