<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page pageEncoding="UTF-8" %>
<layout:overview>
  <jsp:body>
    <tc:box label="#{overviewBundle.sheet_sampleTitle}" id="sheetBox">
      <f:facet name="layout">
        <tc:gridLayout rows="2*;3*"/>
      </f:facet>
      <f:facet name="toolBar">
        <tc:toolBar>
          <tc:toolBarCommand label="Reset" tip="#{overviewBundle.sheet_resetTip}"
                             actionListener="#{overviewController.resetColumnWidths}" />
          <tc:toolBarCommand label="#" tip="#{overviewBundle.sheet_configTip}">
            <tc:attribute name="renderedPartially" value="sheetConfigPopup"/>
            <f:facet name="popup">
              <tc:popup width="300" height="270" id="sheetConfigPopup">
                <tc:box label="#{overviewBundle.sheet_configTitle}">
                  <f:facet name="layout">
                    <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*;fixed"/>
                  </f:facet>
                  <tx:selectOneChoice label="showHeader"
                                      value="#{overviewController.sheetConfig.sheetShowHeader}"
                                      tip="TODO: show Header Text">
                    <f:selectItem itemValue="#{true}" itemLabel="True"/>
                    <f:selectItem itemValue="#{false}" itemLabel="False"/>
                  </tx:selectOneChoice>
                       <tx:in label="first"
                         value="#{overviewController.sheetConfig.sheetFirst}"
                         tip="TODO: paging Start Text">
                    <f:validateLongRange minimum="0"/>
                  </tx:in>
                  <tx:in label="rows"
                         value="#{overviewController.sheetConfig.sheetRows}"
                         tip="TODO: paging Lenght Text">
                    <f:validateLongRange minimum="1"/>
                  </tx:in>

                  <tx:selectOneChoice label="showRowRange"
                                      value="#{overviewController.sheetConfig.sheetRowPagingPosition}"
                                      tip="TODO: paging DirectLinkCount Text">
                    <f:selectItems value="#{overviewController.sheetConfig.sheetPagingPositionItems}"/>
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="showPageRange"
                                      value="#{overviewController.sheetConfig.sheetPagePagingPosition}"
                                      tip="TODO: paging DirectLinkCount Text">
                    <f:selectItems value="#{overviewController.sheetConfig.sheetPagingPositionItems}"/>
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="showDirectLinks"
                                      value="#{overviewController.sheetConfig.sheetDirectPagingPosition}"
                                      tip="TODO: paging DirectLinkCount Text">
                    <f:selectItems value="#{overviewController.sheetConfig.sheetPagingPositionItems}"/>
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="directLinkCount"
                                      value="#{overviewController.sheetConfig.sheetDirectLinkCount}"
                                      tip="TODO: paging DirectLinkCount Text">
                    <f:selectItems value="#{overviewController.sheetConfig.sheetDirectLinkCountItems}"/>
                  </tx:selectOneChoice>

                  <tx:selectOneChoice label="selectable"
                                      value="#{overviewController.sheetConfig.selectable}"
                                      tip="TODO: selectable Text">
                    <f:selectItems value="#{overviewController.sheetConfig.sheetSelectableItems}"/>
                  </tx:selectOneChoice>

                  <tc:cell/>

                  <tc:cell>
                    <f:facet name="layout">
                      <tc:gridLayout columns="100px;1*;100px"
                                     marginLeft="10px" marginRight="10px"/>
                    </f:facet>
                    <tc:button label="Cancel">
                      <tc:attribute name="popupClose" value="immediate"/>
                    </tc:button>
                    <tc:cell/>
                    <tc:button label="Ok">
                      <tc:attribute name="popupClose" value="afterSubmit"/>
                      <tc:attribute name="renderedPartially" value=":page:content:sheetBox"/>
                    </tc:button>
                  </tc:cell>

                </tc:box>
              </tc:popup>
            </f:facet>
          </tc:toolBarCommand>
        </tc:toolBar>
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.sheet_text}"/>

      <tc:sheet value="#{demo.solarList}" id="sheet"
                columns="3*;1*;3*;3*;3*;3*" var="luminary"
                state="#{demo.sheetState}"
                showHeader="#{overviewController.sheetConfig.sheetShowHeader}"
                showRowRange="#{overviewController.sheetConfig.sheetRowPagingPosition}"
                showPageRange="#{overviewController.sheetConfig.sheetPagePagingPosition}"
                showDirectLinks="#{overviewController.sheetConfig.sheetDirectPagingPosition}"
                first="#{overviewController.sheetConfig.sheetFirst}"
                rows="#{overviewController.sheetConfig.sheetRows}"
                directLinkCount="#{overviewController.sheetConfig.sheetDirectLinkCount}"
                stateChangeListener="#{demo.stateChangeListener}"
                sortActionListener="#{overviewController.sheetSorter}"
                selectable="#{overviewController.sheetConfig.selectable}">
<%--
        <f:facet name="reload">
          <tc:reload frequency="5000"/>
        </f:facet>
--%>
        <tc:column label="#{overviewBundle.solarArrayName}" id="name" sortable="true" resizable="false">
          <tc:out value="#{luminary.name}" id="t_name"/>
        </tc:column>
        <tc:column label="#{overviewBundle.solarArrayNumber}" id="number" sortable="false" resizable="false"
                   tip="#{overviewBundle.solarArrayNumberTip}" align="center">
          <tc:out value="#{luminary.number}" id="t_number"/>
        </tc:column>
        <tc:column label="#{overviewBundle.solarArrayOrbit}" sortable="true" id="orbit">
          <f:facet name="menupopup">
            <tc:menu>  
              <tx:menuRadio actionListener="#{demo.filterOrbit}" >
                <f:selectItems value="#{demo.orbitItems}" />
                <tc:attribute name="renderedPartially" value=":page:content:sheet"/>
              </tx:menuRadio>
            </tc:menu>
          </f:facet>
          <tc:panel>
            <f:facet name="click">
              <tc:command actionListener="#{demo.selectOrbit}" >
                <f:param value="#{luminary}" name="luminary"/>
                <tc:attribute name="renderedPartially" value="sheet"/>
              </tc:command>
            </f:facet>
            <tc:out value="#{luminary.orbit}" id="t_orbit"/>            
          </tc:panel>
        </tc:column>
        <tc:column label="#{overviewBundle.solarArrayPopulation}" sortable="true" id="population">
          <tc:in value="#{luminary.population}" id="t_population"/>
        </tc:column>
        <tc:column label="#{overviewBundle.solarArrayDistance}" sortable="true" align="right" id="distance">
          <tc:out value="#{luminary.distance}" id="t_distance"/>
        </tc:column>
        <tc:column label="#{overviewBundle.solarArrayPeriod}" sortable="true" align="right" id="period">
          <tc:out value="#{luminary.period}" id="t_period"/>
        </tc:column>
      </tc:sheet>
    </tc:box>
  </jsp:body>
</layout:overview>
