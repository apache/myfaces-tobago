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

import org.apache.myfaces.tobago.example.data.CommandNode;
import org.apache.myfaces.tobago.example.data.CommandNodeFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class TreeCommandTypesController implements Serializable {

  private CommandNode sample;
  private int actionOneCount = 0;
  private int actionTwoCount = 0;

  public TreeCommandTypesController() {
    sample = CommandNodeFactory.createSample();
  }

  public CommandNode getSample() {
    return sample;
  }

  public int getActionOneCount() {
    return actionOneCount;
  }

  public void increaseActionCount(String name) {
    if ("ActionOne".equals(name)) {
      actionOneCount++;
    } else if ("ActionTwo".equals(name)) {
      actionTwoCount++;
    }
  }

  public int getActionTwoCount() {
    return actionTwoCount;
  }
}
