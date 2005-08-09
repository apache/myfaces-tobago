<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="insolarOuterForm2" >
  <t:panel >
    <f:facet name="layout"><t:gridLayout /></f:facet>
    <t:in value="#{demo.solar.planets[0].sunDistance}"
       label="#{bundle.solarPlanetSunDistance}" />
    <t:in value="#{demo.solar.planets[0].timeOfCirculation}"
       label="#{bundle.solarPlanetTimeOfCirculation}" />
  </t:panel>
</f:subview>
