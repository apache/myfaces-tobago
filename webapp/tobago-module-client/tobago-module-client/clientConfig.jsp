 <%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <tobago:loadBundle basename="tobago" var="tobagoBundle" />
  <tobago:page title="#{tobagoBundle.configTitle}">
    <tobago:panel>
      <f:facet name="layout">
        <tobago:gridlayout />
      </f:facet>
      <tobago:box label="#{tobagoBundle.configTitle}" width="480px" height="450px">
        <f:facet name="layout">
          <tobago:gridlayout />
        </f:facet>

        <tobago:text value="#{tobagoBundle.configThemeText}" />
        <tobago:singleselect value="#{clientConfigController.theme}"
            label="#{tobagoBundle.configTheme}">
          <f:selectItems value="#{clientConfigController.themeItems}" />
        </tobago:singleselect>
        <br />
        <tobago:checkbox value="#{clientConfigController.debugMode}"
            inline="true" label="#{tobagoBundle.configDebugMode}" />
        <br />
        <tobago:text value="#{tobagoBundle.configLocaleText}" />
        <tobago:singleselect value="#{clientConfigController.locale}"
            label="#{tobagoBundle.configLocale}" >
          <f:selectItems value="#{clientConfigController.localeItems}" />
        </tobago:singleselect>
        <br />
        <tobago:text value="#{tobagoBundle.configContentTypeText}" />
        <tobago:singleselect value="#{clientConfigController.contentType}"
            label="#{tobagoBundle.configContentType}">
          <f:selectItems value="#{clientConfigController.contentTypeItems}" />
        </tobago:singleselect>
        <br />

        <tobago:button action="#{clientConfigController.submit}" label="#{tobagoBundle.configSubmit}" />

      </tobago:box>
    </tobago:panel>
  </tobago:page>
</f:view>
