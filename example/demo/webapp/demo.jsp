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
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view locale="#{clientConfigController.locale}">
  <t:loadBundle basename="demo" var="bundle" />
  <t:page label="#{bundle.pageTitle}" id="page" width="750px" >
    
    <t:style style="style/tobago-demo.css" id="demostyle" />

    <t:include value="snip/menubar.jsp"/>

    <f:facet name="layout">
      <t:gridLayout columns="1*;4*" margin="10px" />
    </f:facet>

    <jsp:include page="/snip/navigator.jsp"/>

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>

      <jsp:include page="/snip/header.jsp"/>

      <t:include value="#{demoContent}" />

      <jsp:include page="/snip/footer.jsp"/>

    </t:panel>

  </t:page>
</f:view>
