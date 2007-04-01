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
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="#{controller.language}">
  <tc:loadBundle basename="resource" var="bundle" />

  <tc:page id="page" label="Login" state="#{layout}" width="#{layout.width}" height="#{layout.height}">
    <f:facet name="layout">
      <tc:gridLayout rows="*;fixed;*" columns="*;400px;*"/>
    </f:facet>

    <tc:cell spanX="3"/>
    <tc:cell/>
    <tc:box label="Login">
      <f:facet name="layout">
        <tc:gridLayout rows="fixed;fixed;fixed;fixed"/>
      </f:facet>

      <tc:out value="Use guest/guest or admin/admin to login."/>
      <tx:in id="j_username" label="Username"/>
      <tx:in id="j_password" password="true" label="Password"/>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="*;fixed"/>
        </f:facet>

        <tc:cell/>
        <tc:button label="Login" defaultCommand="true"/>
      </tc:panel>

    </tc:box>
    <tc:cell/>
    <tc:cell spanX="3"/>

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

  </tc:page>
</f:view>
