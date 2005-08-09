<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="test">
    <t:panel>
      <t:out value="#{demo.text[1]}"/>
      <t:out value="Hallo Welt"/>
      <t:link action="button-1" id="button-1" label="Button 1" />
      <t:link action="button-2" id="button-2">
        Button 2
      </t:link>
    </t:panel>
  </t:page>
</f:view>
