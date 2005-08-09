<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="textbox" id="page">

    <t:in value="#{demo.text[0]}" id="text-field" />

    <t:button action="submit" id="submit-button" label="submit" />

  </t:page>
</f:view>
