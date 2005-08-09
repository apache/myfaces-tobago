<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout columns="300px;1*" rows="fixed;1*" />
      </f:facet>

      <t:link type="navigate" action="http://www.atanion.com" label="http://www.atanion.com" />
      <t:cell/>

      <t:cell/>
      <t:cell/>
    </t:panel>

  </jsp:body>
</layout:screenshot>