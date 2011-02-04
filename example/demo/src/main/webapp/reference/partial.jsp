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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <tc:panel id="both">
      <f:facet name="layout">
        <tc:gridLayout rows="20px;300px;*;fixed"/>
      </f:facet>

      <tc:out value="Demonstration and test of partial reload"/>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="1*;1*"/>
        </f:facet>
        <tc:box label="Left panel" id="left">
          <tc:out value="#{partialReloadController.currentDate}"/>
        </tc:box>
        <tc:box label="Right panel" id="right">
          <tc:out value="#{partialReloadController.currentDate}"/>
        </tc:box>
      </tc:panel>

      <tc:out escape="false" value="
        <ul>
          <li> Click on 'Reload Left' button to reload the left panel.</li>
          <li> Click on 'Reload Right' button to reload the right panel.</li>
          <li> Or select a option from the selectOneChoise control:
            <ul>
              <li>Select 'Reload both' to reload both panels</li>
              <li>Select 'Goto prev' to navigate to previous page</li>
              <li>Select 'Goto next' to navigate to next page</li>
            </ul>
          </li>
        </ul>
      "/>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="fixed;1*;140px;1*;fixed"/>
        </f:facet>
        <tc:button label="Reload left"
                   tip="Reload left side box"
                   action="#{partialReloadController.leftAction}">
          <tc:attribute value=":page:content:left" name="renderedPartially"/>
        </tc:button>
        <tc:panel/>
        <tc:selectOneChoice value="#{partialReloadController.navigateAction}">
          <f:facet name="change">
            <tc:command action="#{partialReloadController.navigateAction}">
              <tc:attribute value=":page:content:both" name="renderedPartially"/>
            </tc:command>
          </f:facet>
          <f:selectItem itemLabel="Select action" itemValue=""/>
          <f:selectItem itemLabel="Reload both" itemValue="both"/>
          <f:selectItem itemLabel="Goto prev" itemValue="prev"/>
          <f:selectItem itemLabel="Goto next" itemValue="next"/>
        </tc:selectOneChoice>
        <tc:panel/>
        <tc:button label="Reload right"
                   tip="Reload right side box"
                   action="#{partialReloadController.rightAction}">
          <tc:attribute value=":page:content:right" name="renderedPartially"/>
        </tc:button>
      </tc:panel>
    </tc:panel>
  </jsp:body>
</layout:overview>
