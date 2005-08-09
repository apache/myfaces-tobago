<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="index_jsp" >

  <t:box label="#{bundle.nav_builder}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:button action="#{demo.builder.open}"  label="open..." />

  </t:box>

</f:subview>
