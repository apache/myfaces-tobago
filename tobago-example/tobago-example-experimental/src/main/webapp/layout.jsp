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
  <tc:page >
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout margin="10px" rows="1*;auto;1*" />
      </f:facet>

      <tc:box label="the outer box">

        <f:facet name="layout">
          <tc:gridLayout margin="10px" rows="auto;auto;auto;auto" />
        </f:facet>

        <tc:messages />

        <tx:date label="SearchCriteria" value="#{test.date}" >
          <f:convertDateTime pattern="dd/MM/yyyy" />
           <%--f:facet name="change">
            <tc:command action="#{test.layout}" />
          </f:facet --%>
        </tx:date>
        <tx:in value="#{test.date1}" >
          <f:convertDateTime pattern="dd/MM/yyyy" />
        </tx:in>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout margin="10px" columns="100px;1*;100px;1*;100px" />
          </f:facet>

          <tc:in />
          <tc:button id="test" action="#{test.layout}" label="Search"  />
          <tc:in />
          <tc:button label="suppe" />
          <tc:in />
        </tc:panel>

      </tc:box>
    </tc:panel>
  </tc:page>
</f:view>
