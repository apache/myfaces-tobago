<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.faces.context.FacesContext" %>

<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  facesContext.addMessage("message1", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info message.", "Example of an info message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn message.", "Example of a warn message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error message.", "Example of an error message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage("Message without a severity.", "Example of a message without a severity."));
%>

<layout:overview>
  <jsp:body>
    <tc:box label="Messages must be confirmed here">
      <f:facet name="layout">
        <tc:gridLayout/>
      </f:facet>

      <tc:messages id="mymessages" confirmation="true"/>

    </tc:box>
  </jsp:body>
</layout:overview>
