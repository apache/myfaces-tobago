<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>

      <t:out escape="false" value="#{miniHowtoBundle.introText1}" />

    </t:panel>
  </jsp:body>
</layout:mini-howto>
