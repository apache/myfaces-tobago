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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
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
        <f:facet name="toolBar" >
          <tc:toolBar >
            <tc:toolBarCommand action="#{overviewController.sheetConfig.configSheet}"
                               label="#"
                               tip="#{overviewBundle.sheet_configTip}" />
            <f:facet name="popup" >
              <tc:popup width="300" height="250"
                        rendered="#{overviewController.sheetConfig.sheetConfigPopup}">
                <tc:box label="#{overviewBundle.sheet_configTitle}" >
                  <f:facet name="layout">
                    <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*;fixed"/>
                  </f:facet>
                  <tx:selectOneChoice label="showHeader"
                                      value="#{overviewController.sheetConfig.sheetShowHeader}"
                                      tip="TODO: show Header Text">
                    <f:selectItem itemValue="#{true}" itemLabel="True" />
                    <f:selectItem itemValue="#{false}" itemLabel="False" />
                  </tx:selectOneChoice>
                  <tx:in label="pagingStart"
                         value="#{overviewController.sheetConfig.sheetPagingStart}"
                         tip="TODO: paging Start Text" >
                    <f:validateLongRange minimum="0" />
                  </tx:in>
                  <tx:in label="pagingLength"
                         value="#{overviewController.sheetConfig.sheetPagingLength}"
                         tip="TODO: paging Lenght Text" >
                    <f:validateLongRange minimum="1" />
                  </tx:in>

                  <tx:selectOneChoice label="showRowRange"
                                      value="#{overviewController.sheetConfig.sheetRowPagingPosition}"
                                      tip="TODO: paging DirectLinkCount Text" >
                    <f:selectItems value="#{overviewController.sheetConfig.sheetPagingPositionItems}" />
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="showPageRange"
                                      value="#{overviewController.sheetConfig.sheetPagePagingPosition}"
                                      tip="TODO: paging DirectLinkCount Text" >
                    <f:selectItems value="#{overviewController.sheetConfig.sheetPagingPositionItems}" />
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="showDirectLinks"
                                      value="#{overviewController.sheetConfig.sheetDirectPagingPosition}"
                                      tip="TODO: paging DirectLinkCount Text" >
                    <f:selectItems value="#{overviewController.sheetConfig.sheetPagingPositionItems}" />
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="directLinkCount"
                                      value="#{overviewController.sheetConfig.sheetDirectLinkCount}"
                                      tip="TODO: paging DirectLinkCount Text" >
                    <f:selectItems value="#{overviewController.sheetConfig.sheetDirectLinkCountItems}" />
                  </tx:selectOneChoice>

                  <tc:cell />

                  <tc:cell>
                    <f:facet name="layout">
                      <tc:gridLayout columns="100px;1*;100px"
                                     marginLeft="10px" marginRight="10px"/>
                    </f:facet>
                    <tc:button action="#{overviewController.sheetConfig.configSheet}"
                               immediate="true"
                               label="Cancel" />

                    <tc:cell />
                    <tc:button action="#{overviewController.sheetConfig.configSheet}"
                               label="Ok" />
                  </tc:cell>

                </tc:box>
              </tc:popup>
              </f:facet>
          </tc:toolBar>
        </f:facet>

        <tc:sheet value="#{demo.solarList}" id="sheet"
            columns="3*;1*;3*;3*;3*;3*" var="luminary"
            state="#{demo.sheetState}"
            showHeader="#{overviewController.sheetConfig.sheetShowHeader}"
            showRowRange="#{overviewController.sheetConfig.sheetRowPagingPosition}"
            showPageRange="#{overviewController.sheetConfig.sheetPagePagingPosition}"
            showDirectLinks="#{overviewController.sheetConfig.sheetDirectPagingPosition}"
            pagingStart="#{overviewController.sheetConfig.sheetPagingStart}"
            pagingLength="#{overviewController.sheetConfig.sheetPagingLength}"
            directLinkCount="#{overviewController.sheetConfig.sheetDirectLinkCount}"
            stateChangeListener="#{demo.stateChangeListener}"
            sortActionListener="#{overviewController.sheetSorter}">
          <tc:column label="#{overviewBundle.solarArrayName}" id="name" sortable="true">
            <tc:out value="#{luminary.name}" id="t_name" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayNumber}" id="number" sortable="false" align="center" >
            <tc:out value="#{luminary.number}" id="t_number"/>
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayOrbit}" sortable="true" id="orbit">
            <tc:out value="#{luminary.orbit}" id="t_orbit" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayPopulation}" sortable="true" id="population">
            <tc:in value="#{luminary.population}" id="t_population" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayDistance}" sortable="true" align="right" id="distance">
            <tc:out value="#{luminary.distance}" id="t_distance" />
          </tc:column>
          <tc:column label="#{overviewBundle.solarArrayPeriod}" sortable="true" align="right" id="period">
            <tc:out value="#{luminary.period}" id="t_period" />
          </tc:column>
        </tc:sheet>

      </tc:box>
    </tc:panel>
  </jsp:body>
</layout:overview>
