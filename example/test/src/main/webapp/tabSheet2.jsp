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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<f:view>
  <tc:page id="sheetPage" width="600px" height="200px">
    <tc:tabGroup switchType="reloadTab">
      <tc:tab id="tab1" label="Tab1">
         <tc:out value="Tab 1" />
      </tc:tab>
      <tc:tab id="tab2" label="Tab2">
        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout
              columns="1*;1*"/>
          </f:facet>
          <tc:sheet id="testTable2"
            columns="1*;1*"
            showHeader="true"
            showRowRange="none"
            showPageRange="none"
            showDirectLinks="none"
            first="0"
            selectable="none"
            var="row"
            value="#{test.solarObjects}">
            <tc:column label="Column 1">
              <tc:out id="column1"
                value="#{row.name}"/>
            </tc:column>
            <tc:column label="Column 2">
              <tc:out id="column2"
                value="#{row.name}"/>
            </tc:column>
          </tc:sheet>
          <tc:sheet id="testTable3"
            columns="1*;1*"
            showHeader="true"
            showRowRange="none"
            showPageRange="none"
            showDirectLinks="none"
            first="0"
            selectable="none"
            var="row"
            value="#{test.solarObjects}">
            <tc:column label="Column 1">
              <tc:out id="column1"
                value="#{row.name}"/>
            </tc:column>
            <tc:column label="Column 2">
              <tc:out id="column2"
                value="#{row.name}"/>
            </tc:column>
          </tc:sheet>
        </tc:panel>
      </tc:tab>
    </tc:tabGroup>
  </tc:page>
</f:view>