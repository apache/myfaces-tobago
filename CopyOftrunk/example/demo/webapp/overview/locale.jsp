<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="130px;120px;1*"  />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.locale_text}" />

      <t:box label="#{overviewBundle.locale_title}" height="150px">
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" />
        </f:facet>

        <t:selectOneChoice value="#{clientConfigController2.locale}"
            label="#{overviewBundle.locale}">
          <f:selectItems value="#{clientConfigController2.localeItems}" />
        </t:selectOneChoice>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="100px;*;100px" rows="1*;fixed"/>
          </f:facet>
          <t:cell spanY="2" >
            <t:image value="image/country.gif" width="100px" height="100px" />
          </t:cell>

          <t:cell spanY="2"/>

          <t:cell/>
          <t:button action="#{clientConfigController2.submit}" label="#{overviewBundle.locale_submit}" />
        </t:panel>

      </t:box>
      <t:cell/>
    </t:panel>
  </jsp:body>
</layout:overview>
