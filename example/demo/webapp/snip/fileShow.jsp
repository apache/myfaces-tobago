<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="text_jsp" >

  <t:messages />

  <t:box label="#{bundle.textBox}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:out value="#{demo.fileContent}" />
  </t:box>

</f:subview>
