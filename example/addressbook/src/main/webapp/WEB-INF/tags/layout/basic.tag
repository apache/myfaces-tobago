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

<%@ attribute name="title" %>

<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="#{controller.language}">
  <tc:loadBundle basename="resource" var="bundle"/>

  <tc:page label="${title}" state="#{layout}" width="#{layout.width}" height="#{layout.height}" id="page">
    <f:facet name="menuBar">
      <tc:menuBar>
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
            <tc:menuCheckbox label="#{bundle.menuSettingsMode}" value="#{controller.simple}"/>
          </tc:menu>

          <tc:menu label="#{bundle.menuHelp}">
            <tc:menuItem label="#{bundle.menuHelpAbout}"
                         action="alert('#{bundle.aboutMessage}')"
                         type="script" image="image/org/tango-project/tango-icon-theme/16x16/apps/help-browser.png"/>
          </tc:menu>
        </tc:form>
      </tc:menuBar>
    </f:facet>

    <tc:cell>
      <jsp:doBody/>
    </tc:cell>

  </tc:page>
</f:view>
