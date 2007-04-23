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

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;fixed" columns="2*;*"/>
        </f:facet>
        <tc:out value="#{bundle.loginIntro}"/>
        <tc:link inline="true" onclick="fillInGuest()" label="guest/guest"
                 image="image/org/tango-project/tango-icon-theme/16x16/apps/system-users.png"/>

        <tc:cell/>
        <tc:link inline="true" onclick="fillInAdmin()" label="admin/admin"
                 image="image/org/tango-project/tango-icon-theme/16x16/apps/system-users.png"/>
      </tc:panel>

      <tx:in id="j_username" label="#{bundle.loginUser}"/>
      <tx:in id="j_password" password="true" label="#{bundle.loginPassword}"/>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="*;fixed"/>
        </f:facet>

        <tc:cell/>
        <tc:button label="#{bundle.loginLogin}" defaultCommand="true"/>
      </tc:panel>
    </tc:box>
    <tc:cell/>
    <tc:cell spanX="3"/>

    <tc:script onload="initLoginForm();">
      function initLoginForm() {
        var user = document.getElementById("page:j_username");
        user.name = "j_username";
        var pass = document.getElementById("page:j_password");
        pass.name = "j_password";
        var form = document.getElementById("page::form");
        form.action = "${pageContext.request.contextPath}/j_security_check";
      }
    </tc:script>

    <tc:script>
      function fillInGuest() {
        fillIn("guest");
      }
      function fillInAdmin() {
        fillIn("admin");
      }
      function fillIn(name) {
        var user = document.getElementById("page:j_username");
        user.value = name;
        var pass = document.getElementById("page:j_password");
        pass.value = name;
      }
    </tc:script>

  </tc:page>
</f:view>
