<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view locale="#{clientConfigController.locale}">
  <t:loadBundle basename="demo" var="bundle" />
  <t:page label="#{bundle.pageTitle}" id="page" width="750px" >
    
    <t:style style="style/tobago-demo.css" id="demostyle" />

    <t:include value="snip/menubar.jsp"/>

    <f:facet name="layout">
      <t:gridLayout columns="1*;4*" margin="10px" />
    </f:facet>

    <jsp:include page="/snip/navigator.jsp"/>

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>

      <jsp:include page="/snip/header.jsp"/>

      <t:include value="#{demoContent}" />

      <jsp:include page="/snip/footer.jsp"/>

    </t:panel>

  </t:page>
</f:view>
