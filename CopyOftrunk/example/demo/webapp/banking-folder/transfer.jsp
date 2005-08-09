<%@ page errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="transfer_jsp" >
  <t:tabGroup id="transferTab">
    <t:tab label="#{bundle.bankingTransferSingle}">
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>
      <jsp:include page="transferSingle.jsp" />
    </t:tab>
    <t:tab label="#{bundle.bankingTransferIntern}">
      <jsp:include page="blank.jsp" />
    </t:tab>
    <t:tab label="#{bundle.bankingTransferCollective}">
      <jsp:include page="blank.jsp" />
    </t:tab>
    <t:tab label="#{bundle.bankingTransferTemplate}">
      <jsp:include page="blank.jsp" />
    </t:tab>
  </t:tabGroup>
</f:subview>
