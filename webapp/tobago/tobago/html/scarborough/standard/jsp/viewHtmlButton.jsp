<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="viewHtmlButton_jsp" >
  <t:button id="viewHtmlButton"
        action="window.location = 'view-source:' + window.location"
        type="script" label="HTML" image="image/source.gif"/>
</f:subview>
