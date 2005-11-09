<%@ page import="org.apache.myfaces.tobago.util.VariableResolverUtil"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
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