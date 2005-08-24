<%@ page import="org.apache.myfaces.tobago.util.VariableResolverUtil"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  // load bean before expression in foreach
  org.apache.myfaces.tobago.util.VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), "birdList");
%>
<f:view>
  <t:page>
    <c:forEach items="${birdList.birds}" varStatus="status" >
       <t:out value="#{birdList.birds[${status.index}]}" id="out${status.index}"/>
    </c:forEach>
  </t:page>
</f:view>