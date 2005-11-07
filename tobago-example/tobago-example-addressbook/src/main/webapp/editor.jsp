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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://www.atanion.com/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <tc:loadBundle basename="addressbook" var="bundle" />

  <tc:page label="#{bundle.editorTitle}" width="640px" height="480px">

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="10px;1*;10px" columns="10px;1*;10px"/>
      </f:facet>

      <tc:cell spanX="3" />
      <tc:cell/>

      <tc:box label="#{bundle.editorBoxTitle}" >
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*;fixed"  />
        </f:facet>

        <tc:messages />

        <tx:in value="#{controller.currentAddress.firstName}"
            label="#{bundle.editorFirstName}" required="true" />

        <%--<tx:in label="#{bundle.editorLastName}"--%>
           <%--value="#{controller.currentAddress.lastName}" required="true"/>--%>
        <tx:label value="#{bundle.editorLastName}">
           <tc:in value="#{controller.currentAddress.lastName}" required="true"/>
        </tx:label>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="6*;1*" />
          </f:facet>
          <tx:in value="#{controller.currentAddress.street}"
              label="#{bundle.editorStreet}" />
          <tc:in value="#{controller.currentAddress.houseNumber}" />
        </tc:panel>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="1*;1*" />
          </f:facet>
          <tx:in value="#{controller.currentAddress.zipCode}"
              label="#{bundle.editorCity}" />
          <tc:in value="#{controller.currentAddress.city}" />
        </tc:panel>

        <tc:selectOneChoice value="#{controller.currentAddress.country}"
            label="#{bundle.editorCountry}">
          <f:selectItems value="#{countries}" />
        </tc:selectOneChoice>

        <tx:in value="#{controller.currentAddress.phone}"
            label="#{bundle.editorPhone}" />

        <tx:in value="#{controller.currentAddress.mobile}"
            label="#{bundle.editorMobile}" />

        <tx:in value="#{controller.currentAddress.fax}"
            label="#{bundle.editorFax}" />

        <tx:in value="#{controller.currentAddress.email}"
            label="#{bundle.editorEmail}" />

        <tc:date value="#{controller.currentAddress.dayOfBirth}"
            label="#{bundle.editorBirthday}">
          <f:convertDateTime pattern="dd.MM.yyyy" />
        </tc:date>

        <tx:textarea value="#{controller.currentAddress.note}"
            label="#{bundle.editorNote}" />

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="3*;1*;1*"  />
          </f:facet>

          <tc:cell />
          <tc:button action="store"
              labelWithAccessKey="#{bundle.editorStore}" defaultCommand="true" />
          <tc:button action="cancel" immediate="true"
              labelWithAccessKey="#{bundle.editorCancel}" />
        </tc:panel>

      </tc:box>

      <tc:cell/>
      <tc:cell spanX="3" />

    </tc:panel>
  </tc:page>
</f:view>
