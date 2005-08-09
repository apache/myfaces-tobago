<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="text_jsp" >

  <t:messages />

  <t:box label="#{bundle.textBox}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:file value="#{demo.fileItem}" />
  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button action="#{demo.fileItemUpload}" defaultCommand="true"  label="#{bundle.submit}" />

    <t:cell />

  </t:panel>

</f:subview>
