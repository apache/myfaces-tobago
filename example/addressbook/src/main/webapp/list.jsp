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
<f:view>
  <tc:loadBundle basename="addressbook" var="bundle"/>

  <tc:page label="#{bundle.listPageTitle}" state="#{layout}" width="#{layout.width}" height="#{layout.height}" >

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout margin="10px"/>
      </f:facet>
      <tc:box label="#{bundle.listBoxTitle}" >
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*"/>
        </f:facet>
        <f:facet name="toolBar">
          <tc:toolBar>
            <tc:button label="#{bundle.listNew}" action="#{controller.createAddress}"
                image="image/org/tango-project/tango-icon-theme/16x16/actions/contact-new.png" />
            <tc:button label="#{bundle.listEdit}" action="#{controller.editAddress}"
                image="image/org/tango-project/tango-icon-theme/16x16/apps/accessories-text-editor.png" />
            <tc:button label="#{bundle.listDelete}" action="#{controller.deleteAddresses}"
                image="image/org/tango-project/tango-icon-theme/16x16/places/user-trash.png" >
              <f:facet name="confirmation">
                <tc:out value="#{bundle.listDeleteConfirmation}" />
              </f:facet>
            </tc:button>
            <tc:button label="Select Columns" action="#{controller.selectColumns}">
              <f:facet name="popup">
                <tc:popup width="300px" height="200px" left="200px" top="200px"
                    rendered="#{controller.renderPopup}" id="popup">
                  <f:facet name="layout">
                    <tc:gridLayout rows="fixed;fixed;fixed;1*;fixed" />
                  </f:facet>

                  <tc:selectBooleanCheckbox label="First Name" value="#{controller.renderFirstName}"/>
                  <tc:selectBooleanCheckbox label="Last Name" value="#{controller.renderLastName}"/>
                  <tc:selectBooleanCheckbox label="Birthday" value="#{controller.renderDayOfBirth}"/>
                  <tc:cell/>
                  <tc:panel>
                    <f:facet name="layout">
                      <tc:gridLayout columns="1*;100px;100px" />
                    </f:facet>
                    <tc:cell/>
                    <tc:button action="#{controller.cancelPopup}" label="OK" defaultCommand="true"/>
                    <tc:button action="#{controller.cancelPopup}" label="Cancel" immediate="true"/>
                  </tc:panel>

                </tc:popup>
              </f:facet>
            </tc:button>
          </tc:toolBar>
        </f:facet>

        <tc:messages />

        <tc:sheet columns="1*;1*" value="#{controller.currentAddressList}"
            var="address" state="#{controller.selectedAddresses}">
          <tc:column label="#{bundle.listFirstName}" sortable="true"
                     rendered="#{controller.renderFirstName}">
            <tc:out value="#{address.firstName}" />
          </tc:column>
          <tc:column label="#{bundle.listLastName}" sortable="true"
                     rendered="#{controller.renderLastName}">
            <tc:out value="#{address.lastName}" />
          </tc:column>
          <tc:column label="Birthday" sortable="true"
                     rendered="#{controller.renderDayOfBirth}">
            <tc:out value="#{address.dayOfBirth}">
              <f:convertDateTime pattern="dd.MM.yyyy" />
            </tc:out>
          </tc:column>
        </tc:sheet>

      </tc:box>
    </tc:panel>
  </tc:page>
</f:view>
