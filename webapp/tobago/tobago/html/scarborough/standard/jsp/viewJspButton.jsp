<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%@ taglib uri="http://www.atanion.com/tobago/core" prefix="f" %>
<f:subview id="viewJspButton_jsp" ><%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  String contextPath = facesContext.getExternalContext().getRequestContextPath();
  String sourceName = request.getRequestURI().substring(contextPath.length());
  String action = "/viewSource.jsp?jsp=" + sourceName;
%>
  <tobago:button id="viewJspButton" commandName="<%= action %>" type="navigate">
    <tobago:image value="source.gif" i18n="true" id="image" />
    <f:verbatim >&nbsp;JSP</f:verbatim>
  </tobago:button>
</f:subview>
