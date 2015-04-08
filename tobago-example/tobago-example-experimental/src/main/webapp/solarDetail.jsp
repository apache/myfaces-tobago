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
<f:view>
  <tc:page width="750px" height="300px">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:box >
       <f:facet name="layout">
          <tc:gridLayout columns="1*" rows="auto;auto;auto;auto;auto;auto;auto;auto;auto;*;auto"/>
        </f:facet>
       <tx:in value="#{test.name}" label="Name" readonly="true" />
       <tx:in value="#{test.number}" label="Number" readonly="true" />
       <tx:in value="#{test.orbit}" label="Orbit" readonly="true" />
       <tx:in value="#{test.distance}" label="Distance" readonly="true" />
       <tx:in value="#{test.period}" label="Period" readonly="true" />
       <tx:in value="#{test.incl}" label="Incl" readonly="true" />
       <tx:in value="#{test.eccen}" label="Eccen" readonly="true" />
       <tx:in value="#{test.discoverer}" label="Discoverer" readonly="true" />
       <tx:in value="#{test.discoverYear}" label="DiscoverYear" readonly="true" />  
       <tc:panel/>
        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="4*;1*"  />
          </f:facet>
          <tc:panel />
          <tc:button action="solarList" label="Return To List" defaultCommand="true" />
        </tc:panel>
    </tc:box>
  </tc:page>
</f:view>
