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
import jakarta.faces.event.PhaseId;
import java.io.Serial;

public class FacesEventWrapper extends FacesEvent {

  @Serial
  private static final long serialVersionUID = 1L;

  private FacesEvent wrappedFacesEvent;

  public FacesEventWrapper(final FacesEvent facesEvent, final UIComponent redirectComponent) {
    super(redirectComponent);
    wrappedFacesEvent = facesEvent;
  }

  @Override
  public PhaseId getPhaseId() {
    return wrappedFacesEvent.getPhaseId();
  }

  @Override
  public void setPhaseId(final PhaseId phaseId) {
    wrappedFacesEvent.setPhaseId(phaseId);
  }

  @Override
  public void queue() {
    wrappedFacesEvent.queue();
  }

  @Override
  public String toString() {
    return wrappedFacesEvent.toString();
  }

  @Override
  public boolean isAppropriateListener(
      final FacesListener faceslistener) {
    return wrappedFacesEvent.isAppropriateListener(faceslistener);
  }

  @Override
  public void processListener(final FacesListener faceslistener) {
    wrappedFacesEvent.processListener(faceslistener);
  }

  public FacesEvent getWrappedFacesEvent() {
    return wrappedFacesEvent;
  }
}
