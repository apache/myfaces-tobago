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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="2*;7*" />
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.layout_text}" />
<%-- code-sniplet-start id="layoutManagementExample" --%>
      <tc:box label="#{overviewBundle.layout_exampleTitle}">
        <f:facet name="layout">
          <tc:gridLayout columns="1*;1*" rows="fixed;fixed;fixed;fixed;fixed;*"/>
        </f:facet>
        <tx:selectOneChoice value="#{overviewController.singleValue}"
                           label="#{overviewBundle.layout_salutation}">
          <f:selectItems value="#{overviewController.items}" />
        </tx:selectOneChoice>
        <tc:cell/>

        <tx:in value="" label="#{overviewBundle.layout_firstName}" />
        <tx:in value="" label="#{overviewBundle.layout_lastName}" />

        <tc:cell spanX="2">
          <tx:in value="" label="#{overviewBundle.layout_co}" />
        </tc:cell>

        <tx:in value="" label="#{overviewBundle.layout_street}" />
        <tx:in value="" label="#{overviewBundle.layout_streetNumber}" />

        <tx:in value="" label="#{overviewBundle.layout_zipCode}" />
        <tx:in value="" label="#{overviewBundle.layout_city}" />

        <tc:cell spanX="2">
          <tx:textarea value="" label="#{overviewBundle.layout_note}" />
        </tc:cell>
      </tc:box>
<%-- code-sniplet-end id="layoutManagementExample" --%>
    </tc:panel>
  </jsp:body>
</layout:overview>
