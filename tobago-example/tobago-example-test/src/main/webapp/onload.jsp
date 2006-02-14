<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:page>
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:out value="This page demonstrates the use of the onload/onunload/onexit attributes."/>
    <tc:script onload="alert('onload: You have entered the page');"
        onunload="alert('onunload: You leave the page to another tobago page');"
        onexit="alert('onexit: You leave the page to external');" />
    <tc:button label="click"/>
  </tc:page>
</f:view>
