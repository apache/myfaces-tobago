<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="viewHtmlButton_jsp" >
  <tobago:button id="viewHtmlButton"
        commandName="window.location = 'view-source:' + window.location"
        type="script">
    <tobago:image value="source.gif" i18n="true" />
    &nbsp;HTML
  </tobago:button>
</f:subview>
