<%@ page
  import="org.apache.commons.logging.LogFactory,
          org.apache.commons.logging.Log"
  errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %><%!
  private static final Log log
      = LogFactory.getLog("org.apache.myfaces.tobago.demo.validation.jsp");
  %><%
  try {
%>
<f:subview id="validation_jsp" >

  <t:box label="#{bundle.nav_validation}" height="250px">
    <f:facet name="layout">
      <t:gridLayout columns="1*;1*" rows="fixed;fixed;fixed;*" />
    </f:facet>

    <t:cell spanX="2">
      <t:messages />
    </t:cell>

    <t:in value="#{demo.text[0]}" required="true" id="text_0"  label="#{bundle.validationRequired}" tip="#{bundle.validationRequiredTip}" />
    <t:message for="text_0" />

    <t:in value="#{demo.text[1]}" id="text_1" label="#{bundle.validationRange}" tip="#{bundle.validationRangeTip}">
      <f:validateLongRange minimum="1990" maximum="2020" />
    </t:in>
    <t:message for="text_1" />

    <t:textarea label="#{bundle.validationRequired}" value="#{demo.text[10]}" required="true" id="text_2"  tip="#{bundle.validationRequiredTip}"/>
    <t:message for="text_2" />

  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button label="#{bundle.submit}" />

    <t:cell />

  </t:panel>
</f:subview><%
  }
  catch (Throwable th) {
    log.debug("catched Throwable", th);
  }
%>
