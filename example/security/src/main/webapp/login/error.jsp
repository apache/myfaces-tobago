<%--
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:login title="#{loginBundle.login_error_title}">
  <jsp:body>
  <tc:box label="#{bundle.loginLoginError}">
    <f:facet name="layout">
      <tc:gridLayout rows="*;fixed" />
    </f:facet>

    <tc:out value="#{bundle.loginLoginErrorText}" />

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="1*;100px" />
      </f:facet>

      <tc:cell />

      <tc:button action="/index.jsp" type="navigate" label="#{bundle.loginHome}" />
    </tc:panel>

  </tc:box>
</jsp:body>
</layout:login>
