<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="220px;1*;"/>
      </f:facet>

      <t:out escape="false" value="#{miniHowtoBundle.navigationRulesText1}" />

      <t:box label="#{miniHowtoBundle.navigationRulesBoxTitle1}">
        <t:out value="#{miniHowtoBundle.navigationRules_codeExample1}" />
      </t:box>
    </t:panel>
  </jsp:body>
</layout:mini-howto>
