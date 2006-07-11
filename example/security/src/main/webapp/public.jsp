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

<f:view>
  <tc:page label="Security Demo" width="640px" height="480px">
    <f:facet name="layout">
      <tc:gridLayout margin="10px"/>
    </f:facet>

    <tc:box label="test">
      <f:facet name="layout">
        <tc:gridLayout columns="*;100px;*" rows="fixed;*;fixed;*"/>
      </f:facet>

      <tc:cell spanX="3">
        <tc:out value="Still under development!" />
      </tc:cell>
      <tc:cell spanX="3" />

      <tc:cell/>
      <tc:button label="Login" link="${request.contextPath}/application/index.jsp"/>
      <tc:cell/>

      <tc:cell spanX="3" />

    </tc:box>
  </tc:page>
</f:view>
