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

package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.tobago.util.WebXmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.view.ViewDeclarationLanguage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;

public class AjaxExceptionHandler extends ExceptionHandlerWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private ExceptionHandler wrapped;

  AjaxExceptionHandler(final ExceptionHandler exception) {
    this.wrapped = exception;
  }

  @Override
  public ExceptionHandler getWrapped() {
    return wrapped;
  }

  @Override
  public void handle() throws FacesException {
    final FacesContext facesContext = FacesContext.getCurrentInstance();

    if (facesContext != null
        && facesContext.getPartialViewContext().isAjaxRequest()
        && getUnhandledExceptionQueuedEvents().iterator().hasNext()) {

      final Throwable exception = getUnhandledExceptionQueuedEvents().iterator().next().getContext().getException();

      if (!(exception instanceof AbortProcessingException)) {
        final String errorPageLocation = WebXmlUtils.getErrorPageLocation(exception);

        if (errorPageLocation != null
            && (facesContext.getCurrentPhaseId() != PhaseId.RENDER_RESPONSE
            || !facesContext.getExternalContext().isResponseCommitted())) {

          try {
            final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            request.setAttribute("javax.servlet.error.exception", exception);
            request.setAttribute("javax.servlet.error.exception_type", exception.getClass());
            request.setAttribute("javax.servlet.error.message", exception.getMessage());
            request.setAttribute("javax.servlet.error.request_uri", request.getRequestURI());
            request.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            renderErrorPage(facesContext, errorPageLocation);
            cleanupExceptionQueuedEvents();
          } catch (final IOException e) {
            throw new FacesException(e);
          }
        } else {
          LOG.debug("Can't return an error page. errorPageLocation='{}'", errorPageLocation);
        }
      }
    }

    getWrapped().handle();
  }

  private void renderErrorPage(final FacesContext facesContext, final String errorPageLocation) throws IOException {
    final ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    final UIViewRoot viewRoot = viewHandler.createView(facesContext, errorPageLocation);
    facesContext.setViewRoot(viewRoot);
    facesContext.getPartialViewContext().setRenderAll(true);

    final ViewDeclarationLanguage viewDeclarationLanguage = viewHandler
        .getViewDeclarationLanguage(facesContext, errorPageLocation);
    viewDeclarationLanguage.buildView(facesContext, viewRoot);
    facesContext.getApplication().publishEvent(facesContext, PreRenderViewEvent.class, viewRoot);
    viewDeclarationLanguage.renderView(facesContext, viewRoot);

    facesContext.responseComplete();
  }

  private void cleanupExceptionQueuedEvents() {
    final Iterator<ExceptionQueuedEvent> exceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();
    while (exceptionQueuedEvents.hasNext()) {
      exceptionQueuedEvents.next();
      exceptionQueuedEvents.remove();
    }
  }
}
