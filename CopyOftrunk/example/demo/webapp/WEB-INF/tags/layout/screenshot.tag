<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="#{clientConfigController.locale}">

  <t:loadBundle basename="demo" var="bundle" />

  <t:page label="Screenshot" id="page"
    width="750px" height="600px">
    <f:facet name="layout">
      <t:gridLayout columns="100px;1*" rows="100px;1*" />
    </f:facet>

    <t:cell/>
    <t:cell/>

    <t:cell/>
    <jsp:doBody/>

  </t:page>
</f:view>