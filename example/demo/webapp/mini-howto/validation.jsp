<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="120px;1*;120px" />
      </f:facet>

      <t:out value="#{miniHowtoBundle.validationText1}" escape="false" />

      <t:box label="#{miniHowtoBundle.validationCodeExampleBoxTitle1}">
        <t:out value="#{miniHowtoBundle.validationCodeExample1}" />
      </t:box>

      <t:out value="#{miniHowtoBundle.validationText2}" escape="false" />


    </t:panel>
  </jsp:body>
</layout:mini-howto>
