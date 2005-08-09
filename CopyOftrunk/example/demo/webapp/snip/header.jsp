<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:subview id="header_jsp">
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="*;150px" />
    </f:facet>

    <t:cell />

    <t:link action="/" type="navigate" image="image/tobago_head.gif"
     labelWithAccessKey="#{bundel.hallo}" />
  </t:panel>
</f:subview>
