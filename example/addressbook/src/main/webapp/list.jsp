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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:loadBundle basename="addressbook" var="bundle"/>

  <tc:page label="#{bundle.listPageTitle}" width="640px" height="480px">

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="10px;1*;10px" columns="10px;1*;10px"/>
      </f:facet>

      <tc:cell spanX="3" />
      <tc:cell/>

      <tc:box label="#{bundle.listBoxTitle}" >
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*" />
        </f:facet>
        <f:facet name="toolBar">
          <tc:toolBar>
            <tc:button label="#{bundle.listNew}" action="#{controller.createAddress}" />
            <tc:button label="#{bundle.listEdit}" action="#{controller.editAddress}" />
            <tc:button label="#{bundle.listDelete}" action="#{controller.deleteAddresses}">
              <f:facet name="confirmation">
                <tc:out value="#{bundle.listDeleteConfirmation}" />
              </f:facet>
            </tc:button>
          </tc:toolBar>
        </f:facet>

        <tc:messages />

        <tc:sheet columns="1*;1*" value="#{controller.addressList}"
            var="address" state="#{controller.selectedAddresses}">
          <tc:column label="#{bundle.listFirstName}" sortable="true">
            <tc:out value="#{address.firstName}" />
          </tc:column>
          <tc:column label="#{bundle.listLastName}" sortable="true">
            <tc:out value="#{address.lastName}" />
          </tc:column>
        </tc:sheet>

      </tc:box>

      <tc:cell/>
      <tc:cell spanX="3" />
    </tc:panel>

  </tc:page>
</f:view>
