 <%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <tobago:loadBundle basename="tobago" var="tobagoBundle" />
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
        <tobago:singleselect value="#{tobagoClientController.theme}">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configTheme}" /></f:facet>
          <f:selectItems value="#{tobagoClientController.themeItems}" />
        </tobago:singleselect>
        <br />
        <tobago:checkbox value="#{tobagoClientController.debugMode}" inline="true">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configDebugMode}" inline="true" /></f:facet>
        </tobago:checkbox>
        <br />
        <tobago:text value="#{tobagoBundle.configLanguageText}" />
        <tobago:singleselect value="#{tobagoClientController.language}">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configLanguage}" /></f:facet>
          <f:selectItems value="#{tobagoClientController.languageItems}" />
        </tobago:singleselect>
        <br />
        <tobago:text value="#{tobagoBundle.configContentTypeText}" />
        <tobago:singleselect value="#{tobagoClientController.contentType}">
          <f:facet name="label"><tobago:label value="#{tobagoBundle.configContentType}" /></f:facet>
          <f:selectItems value="#{tobagoClientController.contentTypeItems}" />
        </tobago:singleselect>
        <br />

        <tobago:button action="#{tobagoClientController.submit}">
          <tobago:text value="#{tobagoBundle.configSubmit}" />
        </tobago:button>

      </tobago:groupbox>
    </tobago:panelGroup>
  </tobago:page>
</f:view>
