 <%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.context.ResourceManager,
                       com.atanion.tobago.context.ResourceManager"
%><%@ page errorPage="errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%@ taglib uri="http://www.atanion.com/tobago/core" prefix="f" %>

<jsp:useBean id="tobagoUtilsModel" scope="session"
    class="com.atanion.tobago.context.ClientConfigModel" />
<%
  session.setAttribute("tobagoResourceCacheCoverage",
      new Double(ResourceManager.getInstance().getCacheCoverage()));
%>
<f:view>
  <f:loadBundle basename="tobago" var="tobagoBundle" />
  <tobago:page title="#{tobagoBundle.configTitle}">
    <tobago:panelGroup>
      <f:facet name="layout">
        <tobago:gridlayout />
      </f:facet>
      <tobago:groupbox width="480px" height="450px">
        <f:facet name="label"><tobago:text value="#{tobagoBundle.configTitle}" /></f:facet>
        <f:facet name="layout">
          <tobago:gridlayout columnCount="1" />
        </f:facet>

        <tobago:text value="#{tobagoBundle.configThemeText}" />
        <tobago:singleselect value="#{tobagoUtilsModel.theme}">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configTheme}" /></f:facet>
          <f:selectitems value="#{tobagoUtilsModel.themeItems}" />
        </tobago:singleselect>
        <br />
        <tobago:checkbox value="#{tobagoUtilsModel.jspComment}" inline="true">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configJspComment}" inline="true" /></f:facet>
        </tobago:checkbox>
        <tobago:checkbox value="#{tobagoUtilsModel.debugMode}" inline="true">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configDebugMode}" inline="true" /></f:facet>
        </tobago:checkbox>
        <br />
        <tobago:text value="#{tobagoBundle.configLanguageText}" />
        <tobago:singleselect value="#{tobagoUtilsModel.language}">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configLanguage}" /></f:facet>
          <f:selectitems value="#{tobagoUtilsModel.languageItems}" />
        </tobago:singleselect>
        <br />
        <tobago:text value="#{tobagoBundle.configContentTypeText}" />
        <tobago:singleselect value="#{tobagoUtilsModel.contentType}">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configContentType}" /></f:facet>
          <f:selectitems value="#{tobagoUtilsModel.contentTypeItems}" />
        </tobago:singleselect>
        <tobago:textbox value="#{tobagoResourceCacheCoverage}" readonly="true">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configCachecoverage}" /></f:facet>
        </tobago:textbox>
        <br />

        <tobago:button action="#{tobagoUtilsModel.submit}">
          <tobago:text value="#{tobagoBundle.configSubmit}" />
        </tobago:button>

      </tobago:groupbox>
    </tobago:panelGroup>
  </tobago:page>
</f:view>
