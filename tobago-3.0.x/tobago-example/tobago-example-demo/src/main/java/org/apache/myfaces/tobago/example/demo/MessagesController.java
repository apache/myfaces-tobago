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

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class MessagesController implements Serializable {

  public FacesMessage.Severity getSeverityFatal() {
    return FacesMessage.SEVERITY_FATAL;
  }

  public FacesMessage.Severity getSeverityError() {
    return FacesMessage.SEVERITY_ERROR;
  }

  public FacesMessage.Severity getSeverityWarn() {
    return FacesMessage.SEVERITY_WARN;
  }

  public FacesMessage.Severity getSeverityInfo() {
    return FacesMessage.SEVERITY_INFO;
  }

  public void createFatalMessage() {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal Message", "Details of fatal message."));
  }

  public void createErrorMessage() {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Message", "Details of error message."));
  }

  public void createWarnMessage() {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn Message", "Details of warn message."));
  }

  public void createInfoMessage() {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info Message", "Details of info message."));
  }

  public void createSevenMessages() {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "First Message - Info", null));
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Second Message - Fatal", null));
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Third Message - Warn", null));
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fourth Message - Fatal", null));
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fifth Message - Error", null));
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Sixth Message - Info", null));
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Seventh Message - Warn", null));
  }
}
