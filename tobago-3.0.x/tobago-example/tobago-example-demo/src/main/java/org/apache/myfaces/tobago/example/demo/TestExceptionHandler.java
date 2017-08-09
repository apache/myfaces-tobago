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

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

public class TestExceptionHandler extends ExceptionHandlerWrapper {

  private ExceptionHandler wrapped;

  public TestExceptionHandler(final ExceptionHandler wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public ExceptionHandler getWrapped() {
    return wrapped;
  }

  @Override
  public void handle() throws FacesException {

    final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
    while (iterator.hasNext()) {
      final ExceptionQueuedEvent event = iterator.next();
      final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
      final Throwable cause = context.getException();

      if (cause instanceof ViewExpiredException) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final NavigationHandler nav = facesContext.getApplication().getNavigationHandler();
        try {
          facesContext.addMessage(null, new FacesMessage("The view has been expired!"));

          /*
           * TODO analyse the '/faces'-prefix
           * actually the viewID should be enough, but if you do so, the FacesMessage won't be shown.
           */
          nav.handleNavigation(facesContext, null, "/faces" + ((ViewExpiredException) cause).getViewId());
          facesContext.renderResponse();
        } finally {
          iterator.remove();
        }
      }
    }
    getWrapped().handle();
  }
}
