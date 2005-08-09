<%@ page errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%>
<f:subview id="transferSingle_jsp" >
  <t:selectOneChoice value="#{demo.banking.account}"
      label="#{bundle.bankingAccount}">
    <f:selectItems value="#{demo.banking.accountItems}" />
  </t:selectOneChoice>
  <t:in value="#{demo.banking.transferSingle.name}"
      label="#{bundle.bankingTransferSingleName}" />
  <t:in value="#{demo.banking.transferSingle.number}"
      label="#{bundle.bankingTransferSingleNumber}" />
  <t:in value="#{demo.banking.transferSingle.bankcode}"
      label="#{bundle.bankingTransferSingleBankcode}" />
  <t:in value="#{demo.banking.transferSingle.bankname}"
      disabled="true"  label="#{bundle.bankingTransferSingleBankname}" />
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="3*;1*" />
    </f:facet>
    <t:in value="#{demo.banking.transferSingle.amount}"
        label="#{bundle.bankingTransferSingleAmount}"/>
    <t:in value="#{demo.banking.transferSingle.amountCent}" />
  </t:panel>
  <t:in value="#{demo.banking.transferSingle.text1}" label="#{bundle.bankingTransferSingleText1}" />
  <t:in value="#{demo.banking.transferSingle.text2}" label="#{bundle.bankingTransferSingleText2}" />

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="3*;1*" />
    </f:facet>

    <t:cell />

    <t:button label="#{bundle.bankingTransferSingleNext}" />
  </t:panel>

</f:subview>
