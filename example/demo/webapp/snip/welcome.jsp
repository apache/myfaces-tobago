<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="welcome_jsp" >
  <t:out value="#{bundle.nav_welcome}" />
  <t:out value="#{bundle.welcome_text}" />
  <t:image value="image/tobago_logo.gif"
                border="0" width="270" height="200" />
</f:subview>
