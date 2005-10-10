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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://www.atanion.com/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="1*;3*" />
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.tab_text}" />

    <tc:box label="#{overviewBundle.tab_sampleTitle}" >
      <f:facet name="layout"> <tc:gridLayout /> </f:facet>

      <tc:tabGroup id="tabs" state="#{demo.tabState0}" >

        <tc:tab label="#{overviewBundle.tabClientSide}" >

          <tc:tabGroup id="tabMarsOuterForm" state="#{demo.tabState1}" >
            <tc:tab labelWithAccessKey="#{overviewBundle.tabPlanet}">
              <tc:panel >
                <f:facet name="layout"><tc:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <tc:cell />
                <tx:in value="#{demo.solar.planets[0].diameter}"
                    label="#{overviewBundle.solarPlanetDiameter}" />
                <tx:in value="#{demo.solar.planets[0].mass}"
                    label="#{overviewBundle.solarPlanetMass}" />
                <tc:cell />
              </tc:panel>
            </tc:tab>
            <tc:tab labelWithAccessKey="#{overviewBundle.tabInsolar}">
              <tc:panel >
                <f:facet name="layout"><tc:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <tc:cell />
                <tx:in value="#{demo.solar.planets[0].sunDistance}"
                   label="#{overviewBundle.solarPlanetSunDistance}" />
                <tx:in value="#{demo.solar.planets[0].timeOfCirculation}"
                   label="#{overviewBundle.solarPlanetTimeOfCirculation}" />
                <tc:cell />
              </tc:panel>
            </tc:tab>
            <tc:tab label="#{overviewBundle.tabMoons}" accessKey="#{overviewBundle.tabMoonsAccessKey}">
              <tc:panel>
                <f:facet name="layout"><tc:gridLayout  /></f:facet>
                <tc:sheet value="#{demo.solar.planets[0].moons}"
                  columns="2*;1*;2*;2*" var="moon">
                  <tc:column label="#{overviewBundle.solarArrayName}" id="name" sortable="true">
                    <tc:out value="#{moon.name}" id="t_name" />
                  </tc:column>
                  <tc:column label="#{overviewBundle.solarArrayNumber}" id="number" sortable="false" align="center" >
                    <tc:out value="#{moon.number}" id="t_number"/>
                  </tc:column>
                  <tc:column label="#{overviewBundle.solarArrayDistance}" sortable="true" align="right" >
                    <tc:out value="#{moon.distance}" id="t_distance" />
                  </tc:column>
                  <tc:column label="#{overviewBundle.solarArrayPeriod}" sortable="true" align="right" >
                    <tc:out value="#{moon.period}" id="t_period" />
                  </tc:column>
                </tc:sheet>
              </tc:panel>
            </tc:tab>
          </tc:tabGroup>

        </tc:tab>

        <tc:tab label="#{overviewBundle.tabServerSide}" >

          <tc:tabGroup id="tabMarsOuterForm2" serverside="true" state="#{demo.tabState2}" >

            <tc:tabChangeListener type="org.apache.myfaces.tobago.example.demo.actionlistener.SimpleTabChangeListener"/>

            <tc:tab labelWithAccessKey="#{overviewBundle.tabPlanet}">
              <tc:panel >
                <f:facet name="layout"><tc:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <tc:cell />
                <tx:in value="#{demo.solar.planets[0].diameter}"
                    label="#{overviewBundle.solarPlanetDiameter}" />
                <tx:in value="#{demo.solar.planets[0].mass}"
                    label="#{overviewBundle.solarPlanetMass}" />
                <tc:cell />
              </tc:panel>
            </tc:tab>
            <tc:tab labelWithAccessKey="#{overviewBundle.tabInsolar}">
              <tc:panel >
                <f:facet name="layout"><tc:gridLayout rows="1*;fixed;fixed;1*" /></f:facet>
                <tc:cell />
                <tx:in value="#{demo.solar.planets[0].sunDistance}"
                   label="#{overviewBundle.solarPlanetSunDistance}" />
                <tx:in value="#{demo.solar.planets[0].timeOfCirculation}"
                   label="#{overviewBundle.solarPlanetTimeOfCirculation}" />
                <tc:cell />
              </tc:panel>
            </tc:tab>
            <tc:tab label="#{overviewBundle.tabMoons}">
              <tc:panel>
                <f:facet name="layout"><tc:gridLayout /></f:facet>
                <tc:sheet value="#{demo.solar.planets[0].moons}"
                  columns="2*;1*;2*;2*" var="moon">
                  <tc:column label="#{overviewBundle.solarArrayName}" id="name" sortable="true">
                    <tc:out value="#{moon.name}" id="t_name" />
                  </tc:column>
                  <tc:column label="#{overviewBundle.solarArrayNumber}" id="number" sortable="false" align="center" >
                    <tc:out value="#{moon.number}" id="t_number"/>
                  </tc:column>
                  <tc:column label="#{overviewBundle.solarArrayDistance}" sortable="true" align="right" >
                    <tc:out value="#{moon.distance}" id="t_distance" />
                  </tc:column>
                  <tc:column label="#{overviewBundle.solarArrayPeriod}" sortable="true" align="right" >
                    <tc:out value="#{moon.period}" id="t_period" />
                  </tc:column>
                </tc:sheet>
              </tc:panel>
            </tc:tab>
          </tc:tabGroup>

        </tc:tab>

      </tc:tabGroup>
    </tc:box>

    </tc:panel>
  </jsp:body>
</layout:overview>
