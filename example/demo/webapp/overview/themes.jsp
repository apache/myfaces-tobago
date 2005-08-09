<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
          <t:gridLayout rows="130px;150px;1* " />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.theme_text}" />

      <t:box label="#{overviewBundle.theme_title}" >
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*;fixed" />
        </f:facet>

        <t:selectOneChoice value="#{clientConfigController2.theme}"
            label="#{overviewBundle.theme_label}">
          <f:selectItems value="#{clientConfigController2.themeItems}" />
        </t:selectOneChoice>

        <t:cell/>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="*;100px"   />
          </f:facet>
          <t:cell/>
          <t:button action="#{clientConfigController2.submit}" label="#{overviewBundle.theme_submit}" />
        </t:panel>

      </t:box>
      <t:cell />
    </t:panel>
  </jsp:body>
</layout:overview>
