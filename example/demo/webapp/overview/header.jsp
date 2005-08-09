<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:subview id="header">
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="150px;*;200px" />
    </f:facet>

    <t:link action="overview/intro" immediate="true"
        actionListener="#{overviewNavigation.navigate}"
        image="image/tobago_head.gif" />

    <t:cell />

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="fixed;fixed;*" />
      </f:facet>
      <t:out value="#{clientConfigController.localizedTheme}" />
      <t:out value="#{clientConfigController.localizedLocale}" />
      <t:cell />
    </t:panel>
  </t:panel>
</f:subview>
