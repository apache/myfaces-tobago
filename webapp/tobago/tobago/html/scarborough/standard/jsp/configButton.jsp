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
  <tobago:script file="configWindow.js" i18n="true" />
  <tobago:button commandName="<%= url %>" type="script" id="configButtonAction"> <%-- id is needed for knowledgebase, don't know why :-( --%>
    <tobago:image value="config.gif" i18n="true" />
    <tobago:text value="#{tobagoBundle.configButtonText}" inline="true"
        escape="false" />
  </tobago:button>
</f:subview>