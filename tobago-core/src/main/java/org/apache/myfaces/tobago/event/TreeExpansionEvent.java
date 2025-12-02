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

package org.apache.myfaces.tobago.event;

import jakarta.faces.component.UIComponent;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.FacesListener;
import java.io.Serial;

public class TreeExpansionEvent extends FacesEvent {

  @Serial
  private static final long serialVersionUID = 422186716954088729L;

  private boolean oldExpanded;
  private boolean newExpanded;

  public TreeExpansionEvent(final UIComponent node, final boolean oldExpanded, final boolean newExpanded) {
    super(node);
    this.oldExpanded = oldExpanded;
    this.newExpanded = newExpanded;
  }

  @Override
  public boolean isAppropriateListener(final FacesListener facesListener) {
    return facesListener instanceof TreeExpansionListener;
  }

  @Override
  public void processListener(final FacesListener facesListener) {
    if (facesListener instanceof TreeExpansionListener) {
      if (oldExpanded && !newExpanded) {
        //todo ((TreeExpansionListener) facesListener).treeCollapsed(this);
        ((TreeExpansionListener) facesListener).treeExpanded(this);
      } else if (!oldExpanded && newExpanded) {
        ((TreeExpansionListener) facesListener).treeExpanded(this);
      } else {
        // nothing to do
      }
    }
  }

  public boolean isOldExpanded() {
    return oldExpanded;
  }

  public boolean isNewExpanded() {
    return newExpanded;
  }

  @Override
  public String toString() {
    return super.toString() + "expanded=" + newExpanded;
  }
}
