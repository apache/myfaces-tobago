<%--
 * Copyright 2002-2005 The Apache Software Foundation.
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
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="#{clientConfigController.locale}">
  <tc:loadBundle basename="overview" var="overviewBundle" />
  <tc:page label="#{overviewBundle.pageTitle}" id="page"
      width="750px" height="600px">

    <tc:include value="overview/menubar.jsp" />

    <f:facet name="layout">
      <tc:gridLayout border="0" columns="1*;4*"
        margin="10px" rows="100px;*;fixed"  />
    </f:facet>

    <tc:cell spanX="2">
      <tc:include value="overview/header.jsp"/>
    </tc:cell>

    <tc:cell spanY="2" >
      <tc:include value="overview/navigator.jsp"/>
    </tc:cell>

    <tc:cell>
      <jsp:doBody/>
    </tc:cell>

    <tc:cell>
      <tc:include value="overview/footer.jsp" />
    </tc:cell>

  </tc:page>
</f:view>
