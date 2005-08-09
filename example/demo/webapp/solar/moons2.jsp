<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="moons2" >
  <t:panel>
    <f:facet name="layout"><t:gridLayout /></f:facet>

    <t:sheet value="#{demo.solar.planets[0].moons}"
      columns="2*;1*;2*;2*" var="moon">
      <t:column label="#{bundle.solarArrayName}" id="name" sortable="true">
        <t:out value="#{moon.name}" id="t_name" />
      </t:column>
      <t:column label="#{bundle.solarArrayNumber}" id="number" sortable="false" align="center" >
        <t:out value="#{moon.number}" id="t_number"/>
      </t:column>
      <t:column label="#{bundle.solarArrayDistance}" sortable="true" align="right" >
        <t:out value="#{moon.distance}" id="t_distance" />
      </t:column>
      <t:column label="#{bundle.solarArrayPeriod}" sortable="true" align="right" >
        <t:out value="#{moon.period}" id="t_period" />
      </t:column>
    </t:sheet>

  </t:panel>
</f:subview>
