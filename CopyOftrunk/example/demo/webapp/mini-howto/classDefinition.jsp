<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="80px;1*;80px" />
      </f:facet>

      <t:out escape="false"  value="#{miniHowtoBundle.classDefinitionText1}" />

      <t:box label="#{miniHowtoBundle.classDefinitionBoxTitle}" >
        <t:out value="#{miniHowtoBundle.classDefinitionCodeExample}" />
      </t:box>

      <t:out escape="false"  value="#{miniHowtoBundle.classDefinitionText2}" />

    </t:panel>
  </jsp:body>
</layout:mini-howto>
