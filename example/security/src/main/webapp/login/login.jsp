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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:login title="#{loginBundle.login_login_title}">
  <jsp:body>

  <tc:box label="#{loginBundle.loginLogin}">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>

    <%-- row --%>
    <tx:in id="j_username" required="true"
        label="#{loginBundle.login_username}"/>

    <%-- row --%>
    <tx:in id="j_password" required="true" password="true"
        label="#{loginBundle.login_password}"/>
    <%-- row --%>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="1*;100px"/>
      </f:facet>

      <tc:cell/>

      <tc:button defaultCommand="true"
          label="#{loginBundle.login_login_button}"/>
    </tc:panel>

  </tc:box>

  <tc:script onload="initLoginForm();">
    function initLoginForm() {
      var input_user = document.getElementById("page:j_username");
      input_user.name = "j_username";
      var input_pass = document.getElementById("page:j_password");
      input_pass.name = "j_password";
      var form_element = document.getElementById("page::form");
      form_element.action = "${pageContext.request.contextPath}/j_security_check";
    }
  </tc:script>

  </jsp:body>
</layout:login>
