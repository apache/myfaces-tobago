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

import org.apache.myfaces.tobago.component.DecorationPosition;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class DecorationPositionController implements Serializable {

  private DecorationPosition messagePosition = DecorationPosition.buttonRight;
  private DecorationPosition helpPosition = DecorationPosition.buttonRight;

  public DecorationPosition getMessagePosition() {
    return messagePosition;
  }

  public void setMessagePosition(DecorationPosition messagePosition) {
    this.messagePosition = messagePosition;
  }

  public DecorationPosition getHelpPosition() {
    return helpPosition;
  }

  public void setHelpPosition(DecorationPosition helpPosition) {
    this.helpPosition = helpPosition;
  }

  public DecorationPosition[] getDecorationPositions() {
    return DecorationPosition.values();
  }
}
