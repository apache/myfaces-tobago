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

import org.apache.myfaces.tobago.context.TobagoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIOutput;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PostAddToViewEvent;
import jakarta.faces.event.PostRestoreStateEvent;
import jakarta.faces.event.PreRenderViewEvent;
import java.lang.invoke.MethodHandles;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.ConfigTagDeclaration}
 *
 * @since 5.4.0
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractUIConfig extends UIOutput {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void processEvent(final ComponentSystemEvent event) {

    super.processEvent(event);

    if (event instanceof PreRenderViewEvent || event instanceof PostRestoreStateEvent) {

      final Boolean focusOnError = getFocusOnError();
      if (focusOnError != null) {
        TobagoContext.getInstance(event.getFacesContext()).setFocusOnError(focusOnError);
      }

      final Integer waitOverlayDelayFull = getWaitOverlayDelayFull();
      if (waitOverlayDelayFull != null) {
        TobagoContext.getInstance(event.getFacesContext()).setWaitOverlayDelayFull(waitOverlayDelayFull);
      }

      final Integer waitOverlayDelayAjax = getWaitOverlayDelayAjax();
      if (waitOverlayDelayAjax != null) {
        TobagoContext.getInstance(event.getFacesContext()).setWaitOverlayDelayAjax(waitOverlayDelayAjax);
      }

    } else if (event instanceof PostAddToViewEvent) {
      // NOTE: PreRenderViewEvent can not be used
      getFacesContext().getViewRoot().subscribeToEvent(PreRenderViewEvent.class, this);
    }

  }

  public abstract Boolean getFocusOnError();

  public abstract Integer getWaitOverlayDelayFull();

  public abstract Integer getWaitOverlayDelayAjax();

}
