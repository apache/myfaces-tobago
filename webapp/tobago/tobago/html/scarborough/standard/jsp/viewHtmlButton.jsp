<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="viewHtmlButton_jsp" >
  <tobago:button id="viewHtmlButton"
        action="window.location = 'view-source:' + window.location"
        type="script" label="HTML" image="source.gif"/>
</f:subview>
