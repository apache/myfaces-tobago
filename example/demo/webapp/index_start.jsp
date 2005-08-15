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
<%@ page errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:loadBundle basename="demo" var="bundle" />
  <t:page label="#{bundle.pageTitle}" id="index_start">
    <t:style style="style/tobago-demo.css" id="demostyle" />
    <div id="layer" style="width:100%; height:100%; overflow : auto;" align="center">
      <t:link id="link" action="/welcome.view" type="navigate"
          image="image/tobago_splash.gif" />
    </div>
  </t:page>
</f:view>
