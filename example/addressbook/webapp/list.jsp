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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <t:page label="List of Addresses" width="640px" height="480px">

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="10px;1*;10px" columns="10px;1*;10px"/>
      </f:facet>

      <t:cell spanX="3" />
      <t:cell/>

      <t:box label="Address Editor" >
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" />
        </f:facet>
        <f:facet name="toolBar">
          <t:toolBar>
            <t:button label="New" action="#{controller.createAddress}" />
            <t:button label="Edit" action="#{controller.editAddress}" />
            <t:button label="Delete" action="#{controller.deleteAddresses}">
              <f:facet name="confirmation">
                <t:out value="Do you want to delete the selected addresses?" />
              </f:facet>
            </t:button>
          </t:toolBar>
        </f:facet>

        <t:messages />

        <t:sheet columns="1*;1*" value="#{controller.addressList}"
            var="address" state="#{controller.selectedAddresses}">
          <t:column label="First Name" sortable="true">
            <t:out value="#{address.firstName}" />
          </t:column>
          <t:column label="Last Name" sortable="true">
            <t:out value="#{address.lastName}" />
          </t:column>
        </t:sheet>

      </t:box>

      <t:cell/>
      <t:cell spanX="3" />
    </t:panel>

  </t:page>
</f:view>
