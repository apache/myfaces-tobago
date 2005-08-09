<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="viewSource_jsp" >

  <t:box label="source" height="700px" >
    <f:facet name="layout">
      <t:gridLayout rows="fixed;1*;fixed"  />
    </f:facet>

    <t:in value="#{demo.builder.page}" readonly="true" />

    <t:textarea value="#{demo.builder.source}" />

    <t:button action="#{demo.builder.savePage}"  label="save" />

  </t:box>

</f:subview>
