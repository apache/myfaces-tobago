<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="viewJspButton_jsp" ><%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  String contextPath = facesContext.getExternalContext().getRequestContextPath();
  String sourceName = request.getRequestURI().substring(contextPath.length());
  String action = "/viewSource.jsp?jsp=" + sourceName;
%>
  <tobago:button id="viewJspButton" action="<%= action %>" type="navigate"
      label="JSP" image="source.gif" />
</f:subview>
