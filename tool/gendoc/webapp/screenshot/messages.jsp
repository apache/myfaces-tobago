<%@ page import="javax.faces.context.FacesContext"%>
<%@ page import="javax.faces.application.FacesMessage"%>
<%--
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
--%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  facesContext.addMessage(null, new FacesMessage("Required field"));
%>

<layout:screenshot>
  <f:subview id="messages">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*;fixed" />
        </f:facet>
<%-- code-sniplet-start id="messages" --%>
        <t:messages />
<%-- code-sniplet-end id="messages" --%>
        <t:textarea label="Required Field" required="true" />

        <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;" columns="1*;100px" />
        </f:facet>

         <t:cell/>
         <t:button label="Submit" />
        </t:panel>

        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>