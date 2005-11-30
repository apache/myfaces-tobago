<%--
 * Copyright 2002-2005 The Apache Software Foundation.
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
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="2*;3*" />
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.sheet_text}" />

      <tc:box label="#{overviewBundle.sheet_sampleTitle}" >
        <f:facet name="layout">
          <tc:gridLayout />
        </f:facet>

        <tc:sheet value="#{demo.solarArray}" id="sheet"
            columns="3*;1*;3*;3*;3*;3*" var="luminary"
            state="#{demo.sheetState}"
            showRowRange="left" showPageRange="right" showDirectLinks="center"
            pagingLength="7" directLinkCount="5" >
          <tc:column label="#{overviewBundle.solarArrayName}" id="name" sortable="true">
            <tc:out value="#{luminary.name}" id="t_name" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayNumber}" id="number" sortable="false" align="center" >
            <tc:out value="#{luminary.number}" id="t_number"/>
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayOrbit}" sortable="true" >
            <tc:out value="#{luminary.orbit}" id="t_orbit" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayPopulation}" sortable="true">
            <tc:in value="#{luminary.population}" id="t_population" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayDistance}" sortable="true" align="right" >
            <tc:out value="#{luminary.distance}" id="t_distance" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayPeriod}" sortable="true" align="right" >
            <tc:out value="#{luminary.period}" id="t_period" />
          </tc:column>
        </tc:sheet>

      </tc:box>
    </tc:panel>
  </jsp:body>
</layout:overview>
