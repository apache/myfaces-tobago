<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="125px;1*;115px" />
      </f:facet>

      <t:out escape="false"  value="#{miniHowtoBundle.jspDefinitionText1}" />
      <t:box label="#{miniHowtoBundle.jspDefinitionCodeExampleBoxTitle1}" >
        <t:out value="#{miniHowtoBundle.jspDefinitionCodeExample1}" />
      </t:box>
      <t:out escape="false"  value="#{miniHowtoBundle.jspDefinitionText2}" />
    </t:panel>
  </jsp:body>
</layout:mini-howto>
