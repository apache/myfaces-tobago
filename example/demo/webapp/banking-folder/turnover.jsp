<%@ page import="com.atanion.tobago.demo.model.banking.Transaction,
                 java.util.Date"
    %>
<%@ page errorPage="/errorPage.jsp"
    %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
    %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:subview id="turnover_jsp">
  <t:box>
    <f:facet name="layout">
      <t:gridLayout/>
    </f:facet>

    <t:selectOneChoice value="#{demo.banking.turnoverSelection.account}"
                       id="account" inline="true">
      <t:label value="#{bundle.bankingAccount}" inline="true"/>
      <f:selectItems value="#{demo.banking.turnoverSelection.accountItems}"/>
    </t:selectOneChoice>
    <t:in value="#{demo.banking.turnoverSelection.from}" inline="true">
      <t:label value="#{bundle.bankingTurnoverPeriod}" inline="true"/>
    </t:in>
    <t:in value="#{demo.banking.turnoverSelection.to}" inline="true">
      <t:label value="#{bundle.bankingTurnoverTill}" inline="true"/>
    </t:in>

  </t:box>

  <f:verbatim>
    <br/>
    <div align="right">
  </f:verbatim>
  <t:button action="#{demo.banking.showTurnover}"
            label="#{bundle.bankingTransferSingleNext}"/>
  <f:verbatim>
    </div>
  </f:verbatim>

  <t:panel rendered="#{demo.banking.account.transactions != null}">
    <f:verbatim><hr/></f:verbatim>
    <t:in value="#{demo.banking.account.number}" id="number"
          readonly="true" label="#{bundle.bankingTurnoverNumber}"/>
    <t:in value="#{demo.banking.account.name}" id="name"
          readonly="true" label="#{bundle.bankingTurnoverAccountname}"/>
    <t:in value="#{demo.banking.account.balance.formated}" id="forward"
          readonly="true" label="#{bundle.bankingTurnoverBalance}"/>
    <f:verbatim><hr/></f:verbatim>
    <t:sheet value="#{demo.banking.account.transactions}" var="transaction"
             columns="1*,1*,1*,1*">
      <t:column label="#{bundle.bankingTurnoverDate}" id="date">
        <t:out value="#{transaction.date}"/>
      </t:column>
      <t:column label="#{bundle.bankingTurnoverText}" id="text">
        <t:out value="#{transaction.text}"/>
      </t:column>
      <t:column label="#{bundle.bankingTurnoverType}" id="type">
        <t:out value="#{transaction.type}"/>
      </t:column>
      <t:column label="#{bundle.bankingTurnoverAmount}" align="right"
                id="amount">
        <t:out value="#{transaction.formated}" inline="true"/>
      </t:column>
    </t:sheet>
  </t:panel>
</f:subview>
