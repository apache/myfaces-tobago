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

package org.apache.myfaces.tobago.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.view.ViewDeclarationLanguage;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;

public class TobagoExceptionHandler extends ExceptionHandlerWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private ExceptionHandler wrapped;

  public TobagoExceptionHandler(final ExceptionHandler wrapped) {
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
      final Throwable cause = this.getWrapped().getRootCause(context.getException());
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final NavigationHandler nav = facesContext.getApplication().getNavigationHandler();

      if (cause instanceof ViewExpiredException
          || cause != null && cause.getCause() instanceof ViewExpiredException) {
        final ViewExpiredException viewExpiredException = (ViewExpiredException)
            (cause instanceof ViewExpiredException ? cause : cause.getCause());
        try {
          facesContext.addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_WARN,
                  "The view has been expired!",
                  "Please check the given data or try to start from the beginning."));
          nav.handleNavigation(facesContext, null, viewExpiredException.getViewId());
          facesContext.renderResponse();
          LOG.debug("Handling ViewExpiredException on viewId: {}", viewExpiredException.getViewId());
        } finally {
          iterator.remove();
        }
      } else {
        try {
          final boolean error404 = cause instanceof FileNotFoundException
              || cause != null && cause.getCause() instanceof FileNotFoundException;
          final FacesMessage message;
          if (error404) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "The page was not found!",
                "The requested page was not found!");
            facesContext.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
            LOG.warn("Handling 404 exception.");
          } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "An unknown error has occurred!",
                "An unknown error has occurred!");
            facesContext.getExternalContext().setResponseStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.warn("Handling 500 exception.", cause);
          }
          facesContext.addMessage(null, message);
          final String viewId = "/tobago/error.xhtml";

          // when the rendering was not yet started, we can forward to an error page
          if (event.getContext().getPhaseId().getOrdinal() < PhaseId.RENDER_RESPONSE.getOrdinal()) {
            nav.handleNavigation(facesContext, null, viewId);
            facesContext.renderResponse();
          } else {
            final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.resetBuffer(); // undo rendering, if you can.
            final ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
            final ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(facesContext, viewId);
            final UIViewRoot viewRoot = viewHandler.createView(facesContext, viewId);
            vdl.buildView(facesContext, viewRoot);
            facesContext.getApplication().publishEvent(facesContext, PreRenderViewEvent.class, viewRoot);
            vdl.renderView(facesContext, viewRoot);
            facesContext.responseComplete();
          }
        } catch (Exception e) {
          LOG.error("Exception while exception handling!", e);
        } finally {
          iterator.remove();
        }
      }
    }
    getWrapped().handle();
  }
}
