<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="2*;7*" />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.layout_text}" />

      <t:box label="#{overviewBundle.layout_exampleTitle}">
        <f:facet name="layout">
          <t:gridLayout columns="1*;1*" rows="fixed;fixed;fixed;fixed;fixed;*"/>
        </f:facet>
        <t:selectOneChoice value="#{overviewController.singleValue}" label="#{overviewBundle.layout_salutation}">
          <f:selectItems value="#{overviewController.items}" />
        </t:selectOneChoice>
        <t:cell/>

        <t:in value="" label="#{overviewBundle.layout_firstName}" />
        <t:in value="" label="#{overviewBundle.layout_lastName}" />

        <t:cell spanX="2">
          <t:in value="" label="#{overviewBundle.layout_co}" />
        </t:cell>

        <t:in value="" label="#{overviewBundle.layout_street}" />
        <t:in value="" label="#{overviewBundle.layout_streetNumber}" />

        <t:in value="" label="#{overviewBundle.layout_zipCode}" />
        <t:in value="" label="#{overviewBundle.layout_city}" />

        <t:cell spanX="2">
          <t:textarea value="" label="#{overviewBundle.layout_note}" />
        </t:cell>
      </t:box>

    </t:panel>
  </jsp:body>
</layout:overview>
