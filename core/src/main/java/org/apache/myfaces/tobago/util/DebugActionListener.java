package org.apache.myfaces.tobago.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;

/*
 * Date: Nov 12, 2006
 * Time: 10:44:02 AM
 */
public class DebugActionListener implements ActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(DebugActionListener.class);

  private ActionListener actionListener;


  public DebugActionListener(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("processAction " + actionEvent);
    }
    actionListener.processAction(actionEvent);
  }
}
