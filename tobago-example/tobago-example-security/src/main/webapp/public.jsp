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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:page>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="*;100px;*" rows="auto;*;auto;auto;*"/>
      </f:facet>

      <tc:panel>
        <tc:gridLayoutConstraint columnSpan="3"/>
        <tc:out value="To login to this test application you need a user in the role 'tobago-example-security'." />
      </tc:panel>

      <tc:panel>
        <tc:gridLayoutConstraint columnSpan="3"/>
      </tc:panel>

      <tc:button label="Testing unsecured methodbinding" action="#{controller.doSomethingElse}" />
      <tc:button label="Good Login" link="/application/index.jsp"/>
      <tc:button label="Testing secured methodbinding" action="#{controller.doSomething}" />
      <tc:panel/>
      <tc:messages/>
      <tc:panel/>


      <tc:panel>
        <tc:gridLayoutConstraint columnSpan="3"/>
      </tc:panel>

    </tc:panel>
  </jsp:body>
</layout:page>
