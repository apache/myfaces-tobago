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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTabChangeListener implements TabChangeListener {

  private static final Logger LOG = LoggerFactory.getLogger(SimpleTabChangeListener.class);

  public SimpleTabChangeListener() {
    LOG.info("new SimpleTabChangeListener id='" + System.identityHashCode(this) + "'");
  }

  public void processTabChange(TabChangeEvent tabChangeEvent) {
    LOG.info("TabState has Changed: from tabIndex '" + tabChangeEvent.getOldTabIndex()
        + "' to tabIndex '" + tabChangeEvent.getNewTabIndex() + "' id='" + System.identityHashCode(this) + "'");

  }
}
