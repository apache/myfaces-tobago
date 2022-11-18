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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class LogoutActionListener implements ActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void processAction(final ActionEvent event) throws AbortProcessingException {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final Object session = externalContext.getSession(false);
    if (session != null) {
      if (session instanceof HttpSession) {
        ((HttpSession) session).invalidate();
      }
    }
    final String forward = externalContext.getRequestContextPath() + "/";
    try {
      externalContext.redirect(forward);
    } catch (final IOException e) {
      LOG.error("", e);
      // TODO: may do error handling
      throw new FacesException("Can't redirect to '" + forward + "'");
    }
    facesContext.responseComplete();
  }

}
