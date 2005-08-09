<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="2*;3*" />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.sheet_text}" />

      <t:box label="#{overviewBundle.sheet_sampleTitle}" >
        <f:facet name="layout">
          <t:gridLayout />
        </f:facet>

        <t:sheet value="#{demo.solarArray}" id="sheet"
            columns="3*;1*;3*;3*;3*;3*" var="luminary"
            state="#{demo.sheetState}"
            showRowRange="left" showPageRange="right" showDirectLinks="center"
            pagingLength="7" directLinkCount="5" >
          <t:column label="#{bundle.solarArrayName}" id="name" sortable="true">
            <t:out value="#{luminary.name}" id="t_name" />
          </t:column>
          <t:column label="#{bundle.solarArrayNumber}" id="number" sortable="false" align="center" >
            <t:out value="#{luminary.number}" id="t_number"/>
          </t:column>
          <t:column label="#{bundle.solarArrayOrbit}" sortable="true" >
            <t:out value="#{luminary.orbit}" id="t_orbit" />
          </t:column>
          <t:column label="#{bundle.solarArrayPopulation}" sortable="true">
            <t:in value="#{luminary.population}" id="t_population" />
          </t:column>
          <t:column label="#{bundle.solarArrayDistance}" sortable="true" align="right" >
            <t:out value="#{luminary.distance}" id="t_distance" />
          </t:column>
          <t:column label="#{bundle.solarArrayPeriod}" sortable="true" align="right" >
            <t:out value="#{luminary.period}" id="t_period" />
          </t:column>
        </t:sheet>

      </t:box>
    </t:panel>
  </jsp:body>
</layout:overview>
