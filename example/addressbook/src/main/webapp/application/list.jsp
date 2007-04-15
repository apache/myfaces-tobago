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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view locale="#{controller.language}">
  <tc:loadBundle basename="resource" var="bundle"/>

  <tc:page label="#{bundle.listPageTitle}" state="#{layout}" width="#{layout.width}" height="#{layout.height}">
    <f:facet name="menuBar">
      <tc:menuBar id="menuBar">
        <tc:form>
          <tc:menu label="#{bundle.menuFile}">
            <tc:menuItem label="#{bundle.menuFileNew}" action="#{controller.createAddress}" immediate="true"
                         image="image/org/tango-project/tango-icon-theme/16x16/actions/contact-new.png"/>
            <tc:menuItem label="Add Dummy Addresses" action="#{controller.addDummyAddresses}" immediate="true"/>
            <tc:menuSeparator/>
            <tc:menuItem label="Logout" action="#{controller.logout}" 
                         image="image/org/tango-project/tango-icon-theme/16x16/actions/system-log-out.png"/>
          </tc:menu>

          <tc:menu label="#{bundle.menuSettings}">
            <tc:menu label="#{bundle.menuSettingsLanguage}" >
              <tx:menuRadio action="#{controller.languageChanged}"
                            value="#{controller.language}">
                <f:selectItems value="#{controller.languages}" />
              </tx:menuRadio>
            </tc:menu>
            <tc:menu label="#{bundle.menuSettingsTheme}" >
              <tx:menuRadio action="#{controller.themeChanged}"
                            value="#{controller.theme}">
                <f:selectItems value="#{controller.themeItems}" />
              </tx:menuRadio>
            </tc:menu>
            <tc:menuItem label="#{bundle.menuSettingsMode}" >
              <f:facet name="items">
                <tc:selectBooleanCheckbox value="#{controller.simple}" />
              </f:facet>
            </tc:menuItem>
          </tc:menu>

          <tc:menu label="#{bundle.menuHelp}">
            <tc:menuItem label="#{bundle.menuHelpAbout}"
                         action="alert('#{bundle.aboutMessage}')"
                         type="script" image="image/org/tango-project/tango-icon-theme/16x16/apps/help-browser.png"/>
          </tc:menu>
        </tc:form>
      </tc:menuBar>
    </f:facet>

  <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout margin="10px" rows="fixed;1*;20px"/>
      </f:facet>
      <tc:toolBar iconSize="big">
        <tc:button label="#{bundle.toolbarAddressList}" action="#{controller.search}" immediate="true"
            image="image/org/tango-project/tango-icon-theme/32x32/mimetypes/x-office-address-book.png"/>
        <tc:button label="#{bundle.listNew}" action="#{controller.createAddress}"
            image="image/org/tango-project/tango-icon-theme/32x32/actions/contact-new.png" />
        <tc:button onclick="alert('#{bundle.aboutMessage}')" label="#{bundle.toolbarAbout}"
            image="image/org/tango-project/tango-icon-theme/32x32/apps/help-browser.png"/>
        <tc:button label="#{bundle.admin}" action="#{admin.admin}"
            image="image/org/tango-project/tango-icon-theme/32x32/categories/applications-system.png" />
      </tc:toolBar>
      <tc:box label="#{bundle.listBoxTitle}">
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
                image="image/org/tango-project/tango-icon-theme/16x16/places/user-trash.png">
              <f:facet name="confirmation">
                <tc:out value="#{bundle.listDeleteConfirmation}" />
              </f:facet>
            </tc:button>
            <tc:button label="Select Columns" action="#{controller.selectColumns}">
              <f:facet name="popup">
                <tc:popup width="300px" height="200px" left="200px" top="200px"
                    rendered="#{controller.renderPopup}" id="popup">
                  <tc:box label="Select Columns">
                    <f:facet name="layout">
                      <tc:gridLayout rows="fixed;fixed;fixed;1*;fixed" margin="10" />
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
                  </tc:box>
                </tc:popup>
              </f:facet>
            </tc:button>
          </tc:toolBar>
        </f:facet>

        <tc:messages />

        <tc:sheet columns="1*;1*;1*" value="#{controller.currentAddressList}"
            var="address" state="#{controller.selectedAddresses}"
            sortActionListener="#{controller.sheetSorter}">
          <tc:column id="firstName" label="#{bundle.listFirstName}" sortable="true"
                     rendered="#{controller.renderFirstName}">
            <tc:out value="#{address.firstName}" />
          </tc:column>
          <tc:column id="lastName" label="#{bundle.listLastName}" sortable="true"
                     rendered="#{controller.renderLastName}">
            <tc:out value="#{address.lastName}" />
          </tc:column>
          <tc:column id="dayOfBirth" label="Birthday" sortable="true"
                     rendered="#{controller.renderDayOfBirth}">
            <tc:out value="#{address.dayOfBirth}">
              <f:convertDateTime pattern="#{bundle.editorDatePattern}" />
            </tc:out>
          </tc:column>
        </tc:sheet>

      </tc:box>
      <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="*;20px;*"/>
      </f:facet>
      <tc:form>
        <tx:selectOneChoice label="#{bundle.footerLanguage}"
               value="#{controller.language}">
          <f:selectItems value="#{controller.languages}" />
          <f:facet name="change">
            <tc:command action="#{controller.languageChangedList}"/>
          </f:facet>
        </tx:selectOneChoice>
      </tc:form>
      <tc:image alt="#{bundle.footerFlag}" width="16" height="11"
                value="#{bundle.footerFlagIcon}"/>
      <tc:form>
        <tx:selectOneChoice label="#{bundle.footerTheme}" value="#{controller.theme}">
          <f:selectItems value="#{controller.themeItems}" />
          <f:facet name="change">
            <tc:command action="#{controller.themeChanged}"/>
          </f:facet>
        </tx:selectOneChoice>
      </tc:form>
    </tc:panel>
    </tc:panel>
  </tc:page>
</f:view>
