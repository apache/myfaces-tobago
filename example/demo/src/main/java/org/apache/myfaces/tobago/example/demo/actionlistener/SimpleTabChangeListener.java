/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.example.demo.actionlistener;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.StateChangeEvent;

/**
 * User: weber
 * Date: 13.12.2004
 * Time: 16:55:44
 */
public class SimpleTabChangeListener implements TabChangeListener {

  private static final Log LOG = LogFactory.getLog(SimpleTabChangeListener.class);

  public void processStateChange(StateChangeEvent stateChangeEvent) {
    LOG.info("TabState has Changed: from " + stateChangeEvent.getOldState()
        + " to " + stateChangeEvent.getNewState());

  }
}
