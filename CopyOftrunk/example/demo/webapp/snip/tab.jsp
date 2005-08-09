<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="tab" >

  <t:box label="Mars with a form outside of the tabs"
      id="outerform" height="350" >
    <f:facet name="layout">
      <t:gridLayout rows="1*;fixed"  />
    </f:facet>

    <jsp:include page="tab2.jsp" />

    <%-- button --%>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout columns="100px;*" />
      </f:facet>

      <t:button label="#{bundle.submit}" />

      <t:cell />

    </t:panel>
  </t:box>

</f:subview>
