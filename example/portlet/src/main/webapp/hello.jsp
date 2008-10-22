<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page width="300px" height="50px">
    <f:facet name="layout">
      <tc:gridLayout columns="*;fixed" rows="fixed;*"/>
    </f:facet>
    <tc:out value="Hello #{user.name}!"/>
    <tc:button action="helloWorld" label="Return"/>
    <tc:cell/>
    <tc:cell/>
  </tc:page>
</f:view>
