<%@ page import="javax.faces.application.ViewHandler,
                 com.atanion.tobago.context.ResourceManagerUtil,
                 javax.faces.context.FacesContext"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="configButton_jsp" >
  <t:loadBundle basename="tobago" var="tobagoBundle" />
<%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  ViewHandler viewHandler = facesContext.getApplication().getViewHandler();

  String clientConfigJsp = viewHandler.getActionURL(
      facesContext, "/tobago-module-client/clientConfig.jsp");

  String url = "configWindow('" + clientConfigJsp + "')";
%>
  <t:script file="script/configWindow.js" />
  <%-- id is needed for knowledgebase, don't know why :-( --%>
  <t:button action="<%= url %>" type="script" id="configButtonAction"
        label="#{tobagoBundle.configButtonText}"
        image="image/config.gif" />
</f:subview>