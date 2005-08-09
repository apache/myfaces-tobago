<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:loadBundle basename="???" var="bundle" />
  <t:page label="text">
    <t:out value="#{bundle.atanion}"/>
    <t:out value="#{bundle.atanionPoweredBy}"/>
  </t:page>
</f:view>
