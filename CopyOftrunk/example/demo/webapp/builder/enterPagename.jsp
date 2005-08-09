<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="enterPagename_jsp" >

  <t:box label="please enter the page to edit">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:in value="#{demo.builder.page}" label="page" />

    <t:button action="#{demo.builder.loadPage}" label="ok" />

  </t:box>

</f:subview>
