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

package org.apache.myfaces.tobago.example.demo.actionlistener;

import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;

public class SimpleTabChangeListener implements TabChangeListener {

  private int count = 0;
  private int newTabIndex = -1;
  private int oldTabIndex = -1;
  private String clientId;

  @Override
  public void processTabChange(TabChangeEvent tabChangeEvent) {
    count++;
    newTabIndex = tabChangeEvent.getNewTabIndex();
    oldTabIndex = tabChangeEvent.getOldTabIndex();
    clientId = tabChangeEvent.getComponent().getClientId();
  }

  public int getCount() {
    return count;
  }

  public int getNewTabIndex() {
    return newTabIndex;
  }

  public int getOldTabIndex() {
    return oldTabIndex;
  }

  public String getClientId() {
    return clientId;
  }
}
