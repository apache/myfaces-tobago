<%@ page import="org.apache.myfaces.tobago.util.VariableResolverUtil"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  // load bean before expression in foreach
  VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), "birdList");
%>
<f:view>
  <tc:page>
    <f:facet name="layout">
      <tc:gridLayout />
    </f:facet>
    <c:forEach items="${birdList.birds}" varStatus="status" >
       <tc:out value="#{birdList.birds[${status.index}]}" id="out${status.index}"/>
    </c:forEach>
  </tc:page>
</f:view>