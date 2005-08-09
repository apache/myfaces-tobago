<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<jsp:useBean id="simple"
    class="com.atanion.tobago.demo.model.Simple" scope="session" />
<f:view>
  <t:page label="textbox" id="page">

    <t:in value="#{simple.name}" id="text-field" required="true" />
    <t:button action="submit" id="submit-button">submit</t:button>

  </t:page>
</f:view>
