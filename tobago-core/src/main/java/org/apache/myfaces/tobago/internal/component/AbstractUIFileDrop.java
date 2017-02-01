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

package org.apache.myfaces.tobago.internal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.component.SupportsRenderedPartially;

import javax.faces.component.ActionSource2;
import javax.faces.event.ActionListener;

public abstract class AbstractUIFileDrop extends AbstractUIFile implements SupportsRenderedPartially, ActionSource2 {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIFileDrop.class);

  public enum VisibleType {
    DROP_ZONE,
    FILE,
    BUTTON,
    IMAGE,
    NONE;

    public static VisibleType asEnum(String value) {
      if (value != null) {
        try {
          return valueOf(value);
        } catch (Exception e) {
          LOG.warn("Caught: {}", e.getMessage());
        }
      }
      return DROP_ZONE;
    }
  }

  public abstract String getDropZoneId();
  public abstract String getVisibleType();

  public void addActionListener(ActionListener listener) {
    this.addFacesListener(listener);
  }

  public void removeActionListener(ActionListener listener) {
    this.removeFacesListener(listener);
  }

  public ActionListener[] getActionListeners() {
    return (ActionListener[])((ActionListener[])this.getFacesListeners(ActionListener.class));
  }

}
