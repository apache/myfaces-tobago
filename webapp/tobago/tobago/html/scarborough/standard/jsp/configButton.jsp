<%@ page import="javax.faces.application.ViewHandler,
                 com.atanion.tobago.context.ResourceManagerUtil,
                 javax.faces.context.FacesContext"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="configButton_jsp" >
  <tobago:loadBundle basename="tobago" var="tobagoBundle" />
<%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  ViewHandler viewHandler = facesContext.getApplication().getViewHandler();

  String clientConfigJsp = viewHandler.getActionURL(
      facesContext, "/tobago-module-client/clientConfig.jsp");

  String url = "configWindow('" + clientConfigJsp + "')";
%>
  <tobago:script file="script/configWindow.js" />
  <%-- id is needed for knowledgebase, don't know why :-( --%>
  <tobago:button action="<%= url %>" type="script" id="configButtonAction"
        label="#{tobagoBundle.configButtonText}"
        image="image/config.gif" />
</f:subview>