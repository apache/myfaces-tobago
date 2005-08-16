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
  <t:page label="#{bundle.title}" width="640px" height="480px">

  <t:loadBundle basename="addressbook-demo" var="bundle"/>

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="10px;1*;10px" columns="10px;1*;10px"/>
      </f:facet>

      <t:cell spanX="3" />
      <t:cell/>

      <t:box label="Address Editor" >
        <f:facet name="layout">
          <t:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*;fixed"  />
        </f:facet>

        <t:messages />

        <t:in value="#{controller.currentAddress.firstName}"
            label="First Name" required="true" />

        <t:in value="#{controller.currentAddress.lastName}"
            label="Last Name" required="true" rendered="true" />

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="6*;1*" />
          </f:facet>
          <t:in value="#{controller.currentAddress.street}"
              label="Street / No" />
          <t:in value="#{controller.currentAddress.houseNumber}" />
        </t:panel>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="1*;1*" />
          </f:facet>
          <t:in value="#{controller.currentAddress.zipCode}"
              label="ZIP / City" />
          <t:in value="#{controller.currentAddress.city}" />
        </t:panel>

        <t:selectOneChoice value="#{controller.currentAddress.country}"
            label="Country">
          <f:selectItems value="#{countries}" />
        </t:selectOneChoice>

        <t:in value="#{controller.currentAddress.phone}"
            label="Phone" />

        <t:in value="#{controller.currentAddress.mobile}"
            label="Mobile" />

        <t:in value="#{controller.currentAddress.fax}"
            label="Fax" />

        <t:in value="#{controller.currentAddress.email}"
            label="Email" />

        <t:date value="#{controller.currentAddress.dayOfBirth}"
            label="Day of Birth">
          <f:convertDateTime pattern="dd.MM.yyyy" />
        </t:date>

        <t:textarea value="#{controller.currentAddress.note}"
            label="Note" />

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="3*;1*;1*"  />
          </f:facet>

          <t:cell />
          <t:button action="#{controller.storeAddress}"
              label="Store" defaultCommand="true" />
          <t:button action="list" immediate="true"
              labelWithAccessKey="_Cancel" />
        </t:panel>

      </t:box>

      <t:cell/>
      <t:cell spanX="3" />

    </t:panel>
  </t:page>
</f:view>
