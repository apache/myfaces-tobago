<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="1*;3*" />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.tab_text}" />

    <t:box label="#{overviewBundle.tab_sampleTitle}" >
      <f:facet name="layout"> <t:gridLayout /> </f:facet>

      <t:tabGroup id="tabs" state="#{demo.tabState0}" >

        <t:tab label="#{bundle.tabClientSide}" >

          <t:tabGroup id="tabMarsOuterForm" state="#{demo.tabState1}" >
            <t:tab labelWithAccessKey="#{bundle.tabPlanet}">
              <t:panel >
                <f:facet name="layout"><t:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <t:cell />
                <t:in value="#{demo.solar.planets[0].diameter}"
                    label="#{bundle.solarPlanetDiameter}" />
                <t:in value="#{demo.solar.planets[0].mass}"
                    label="#{bundle.solarPlanetMass}" />
                <t:cell />
              </t:panel>
            </t:tab>
            <t:tab labelWithAccessKey="#{bundle.tabInsolar}">
              <t:panel >
                <f:facet name="layout"><t:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <t:cell />
                <t:in value="#{demo.solar.planets[0].sunDistance}"
                   label="#{bundle.solarPlanetSunDistance}" />
                <t:in value="#{demo.solar.planets[0].timeOfCirculation}"
                   label="#{bundle.solarPlanetTimeOfCirculation}" />
                <t:cell />
              </t:panel>
            </t:tab>
            <t:tab label="#{bundle.tabMoons}" accessKey="#{bundle.tabMoonsAccessKey}">
              <t:panel>
                <f:facet name="layout"><t:gridLayout  /></f:facet>
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
            </t:tab>
          </t:tabGroup>

        </t:tab>

        <t:tab label="#{bundle.tabServerSide}" >

          <t:tabGroup id="tabMarsOuterForm2" serverside="true" state="#{demo.tabState2}" >

            <t:tabChangeListener type="org.apache.myfaces.tobago.demo.actionlistener.SimpleTabChangeListener"/>

            <t:tab labelWithAccessKey="#{bundle.tabPlanet}">
              <t:panel >
                <f:facet name="layout"><t:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <t:cell />
                <t:in value="#{demo.solar.planets[0].diameter}"
                    label="#{bundle.solarPlanetDiameter}" />
                <t:in value="#{demo.solar.planets[0].mass}"
                    label="#{bundle.solarPlanetMass}" />
                <t:cell />
              </t:panel>
            </t:tab>
            <t:tab labelWithAccessKey="#{bundle.tabInsolar}">
              <t:panel >
                <f:facet name="layout"><t:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <t:cell />
                <t:in value="#{demo.solar.planets[0].sunDistance}"
                   label="#{bundle.solarPlanetSunDistance}" />
                <t:in value="#{demo.solar.planets[0].timeOfCirculation}"
                   label="#{bundle.solarPlanetTimeOfCirculation}" />
                <t:cell />
              </t:panel>
            </t:tab>
            <t:tab label="#{bundle.tabMoons}">
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
            </t:tab>
          </t:tabGroup>

        </t:tab>

      </t:tabGroup>
    </t:box>

    </t:panel>
  </jsp:body>
</layout:overview>
