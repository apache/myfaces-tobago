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
/*
 * Created 25.10.2004 17:57:53.
 * $Id$
 */
package org.apache.myfaces.tobago.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.FacesException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutActionListener implements ActionListener {

  private static final Log LOG = LogFactory.getLog(LogoutActionListener.class);

  public void processAction(ActionEvent event) throws AbortProcessingException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    HttpSession session = (HttpSession) externalContext.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    String forward = externalContext.getRequestContextPath() + "/";
    try {
      externalContext.redirect(forward);
    } catch (IOException e) {
      LOG.error("", e);
      // todo: may do error handling
      throw new FacesException("Can't redirect to '" + forward + "'");
    }
    facesContext.responseComplete();
  }

}
