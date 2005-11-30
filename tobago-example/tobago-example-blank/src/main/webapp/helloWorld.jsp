<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page>
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:out value="Hello World"/>
  </tc:page>
</f:view>