<%@ page import="javax.faces.context.FacesContext,
                 org.apache.myfaces.tobago.context.ClientProperties"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%

  ClientProperties client = ClientProperties.getInstance(
      FacesContext.getCurrentInstance().getViewRoot());
  boolean oldDebugMode = client.isDebugMode();
  String debugMode = request.getParameter("setDebugMode");
  if ("true".equals(debugMode) ) {
    client.setDebugMode(true);
  } else if ("false".equals(debugMode) ) {
    client.setDebugMode(false);
  }
%>
<f:view>
  <t:page label="textbox" id="page">

    <h3> Page with model error! </h3>

    <t:in value="#{NoModel.NoText[0]}" id="text-field"  />

  </t:page>
</f:view>

<%
  client.setDebugMode(oldDebugMode); // restore debug mode
%>
