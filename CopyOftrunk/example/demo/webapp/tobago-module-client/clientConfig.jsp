 <%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: clientConfig.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:loadBundle basename="tobago" var="tobagoBundle" />
  <t:page label="#{tobagoBundle.configTitle}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:box label="#{tobagoBundle.configTitle}" width="480px" height="450px">
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>

      <t:out value="#{tobagoBundle.configThemeText}" />
      <t:selectOneChoice value="#{clientConfigController.theme}"
          label="#{tobagoBundle.configTheme}">
        <f:selectItems value="#{clientConfigController.themeItems}" />
      </t:selectOneChoice>
      <br />
      <t:selectBooleanCheckbox value="#{clientConfigController.debugMode}"
          inline="true" label="#{tobagoBundle.configDebugMode}" />
      <br />
      <t:out value="#{tobagoBundle.configLocaleText}" />
      <t:selectOneChoice value="#{clientConfigController.locale}"
          label="#{tobagoBundle.configLocale}" >
        <f:selectItems value="#{clientConfigController.localeItems}" />
      </t:selectOneChoice>
      <br />
      <t:out value="#{tobagoBundle.configContentTypeText}" />
      <t:selectOneChoice value="#{clientConfigController.contentType}"
          label="#{tobagoBundle.configContentType}">
        <f:selectItems value="#{clientConfigController.contentTypeItems}" />
      </t:selectOneChoice>
      <br />

      <t:button action="#{clientConfigController.submit}" label="#{tobagoBundle.configSubmit}" />

    </t:box>
  </t:page>
</f:view>
