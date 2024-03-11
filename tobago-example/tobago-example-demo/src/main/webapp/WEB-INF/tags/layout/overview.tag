<%--
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>

<f:view locale="#{clientConfigController.locale}">
  <tc:loadBundle basename="overview" var="overviewBundle"/>
  <tc:page applicationIcon="icon/favicon.ico" label="#{overviewBundle.pageTitle} - #{title}" id="page">
    <%-- fixme: #{title} will not evaluated correctly, because it will be evaluated "late", but too late
         fixme: and ${title} is not allowed, because it can't be provided with JSF 1.2 
         fixme: With facelets this works. --%>
    <f:facet name="resize">
      <tc:command immediate="true"/>
    </f:facet>

    <f:facet name="menuBar">
      <tc:menuBar>
        <tc:form>
          <tc:menu label="#{overviewBundle.menu_config}">
            <tc:menu label="#{overviewBundle.menu_themes}">
              <tx:menuRadio action="#{clientConfigController.submit}" value="#{clientConfigController.theme}">
                <f:selectItems value="#{clientConfigController.themeItems}"/>
              </tx:menuRadio>
            </tc:menu>
            <tc:menu label="#{overviewBundle.menu_locale}">
              <tx:menuRadio action="#{clientConfigController.submit}" value="#{clientConfigController.locale}">
                <f:selectItems value="#{clientConfigController.localeItems}"/>
              </tx:menuRadio>
            </tc:menu>
            <tc:menuCommand action="#{demo.resetSession}" label="Reset"/>
          </tc:menu>

          <tc:menu label="#{overviewBundle.menu_help}">
            <tc:menuCommand
                onclick="alert('#{overviewBundle.pageTitle}' + String.fromCharCode(10) + '#{info.version}' + String.fromCharCode(10) + '#{overviewBundle.tobago_url}' + String.fromCharCode(10))"
                label="#{overviewBundle.menu_about}"/>
            <tc:menuCommand link="http://myfaces.apache.org/tobago" label="Tobago in the Web"/>
            <tc:menuCommand action="/server-info.xhtml" immediate="true"
                            label="Server Info" disabled="#{! info.enabled}"/>
            <tc:menuCommand action="/logging-info.xhtml" immediate="true"
                            label="Logging Info" disabled="#{! info.enabled}"/>
          </tc:menu>
        </tc:form>
      </tc:menuBar>

    </f:facet>
    <f:facet name="layout">
      <tc:gridLayout border="0" columns="*;4*" margin="10px" rows="100px;auto;*;auto"/>
    </f:facet>

    <tc:panel>
      <tc:gridLayoutConstraint columnSpan="2"/>
      <jsp:include page="/header.jsp"/>
    </tc:panel>

    <tc:panel>
      <tc:gridLayoutConstraint rowSpan="3"/>
      <jsp:include page="/navigation.jsp"/>
    </tc:panel>

    <tc:messages/>

    <tc:box label="(Label doesn't work in the JSP version) #{title}">
      <jsp:doBody/>
    </tc:box>

    <tc:panel>
      <jsp:include page="/footer.jsp"/>
    </tc:panel>

  </tc:page>
</f:view>