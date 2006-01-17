<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:page>
    <f:facet name="layout">
      <tc:gridLayout columns="100px;30px" margin="20px" />
    </f:facet>

    <tx:in label="Selected:" value="#{fishPond.selectedFish}" readonly="true" />
    <tc:button label="random" action="#{fishPond.random}" />
  </tc:page>
</f:view>
