<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="planetOuterForm2" >
  <t:panel >
    <f:facet name="layout"><t:gridLayout /></f:facet>
    <t:in value="#{demo.solar.planets[0].diameter}"
        label="#{bundle.solarPlanetDiameter}" />
    <t:in value="#{demo.solar.planets[0].mass}"
        label="#{bundle.solarPlanetMass}" />
  </t:panel>
</f:subview>
