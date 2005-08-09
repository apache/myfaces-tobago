<%@ page errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="banking_jsp" >
  <f:verbatim>
    <b>Banking</b>
  </f:verbatim>

  <t:tabGroup id="bankingTab">
    <t:tab label="#{bundle.bankingTurnover}">
      <jsp:include page="turnover.jsp" />
    </t:tab>
    <t:tab label="#{bundle.bankingTransfer}">
      <jsp:include page="transfer.jsp" />
    </t:tab>
    <t:tab label="#{bundle.bankingStandingOrder}">
      <jsp:include page="blank.jsp" />
    </t:tab>
  </t:tabGroup>

</f:subview>
