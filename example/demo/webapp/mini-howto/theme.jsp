<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="80px;150px;1*" />
      </f:facet>

      <t:out value="#{miniHowtoBundle.themesText1}" escape="false" />

      <t:box label="#{miniHowtoBundle.themesCodeExampleBoxTitle1}">
        <t:out value="#{miniHowtoBundle.themesCodeExample1}" />
      </t:box>

      <t:out value="#{miniHowtoBundle.themesText2}" escape="false" />
    </t:panel>
  </jsp:body>
</layout:mini-howto>
