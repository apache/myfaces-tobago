<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="textarea" id="page">

    <t:textarea value="#{demo.text[1]}" id="text-field"  />

    <t:button id="submit-button" label="#{bundle.submit}" />

  </t:page>
</f:view>
