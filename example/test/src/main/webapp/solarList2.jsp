<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:page width="750px" height="300px">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:box >
       <tc:sheet binding="#{test.table}" value="#{test.solarObjects}"
            columns="1*;1*;1*;1*;1*;1*;1*;1*;2*" var="solarObject"
            showHeader="true"  showPageRange="center" rows="10" >
        </tc:sheet>
    </tc:box>
  </tc:page>
</f:view>
