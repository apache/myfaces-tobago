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
<f:view>
  <tc:page width="750px" height="300px">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:box >
       <tc:sheet value="#{test.solarObjects}" id="sheet"
            columns="1*;1*;1*;1*;1*;1*;1*;1*;2*" var="solarObject"
            showHeader="true"  showPageRange="center" rows="10" >
          <tc:column label="Name" >
             <tc:link label="#{solarObject.name}" link="SolarDetail">
               <f:param value="#{solarObject.name}" name="id" />
             </tc:link>
          </tc:column>
          <tc:column label="Number" align="center" >
            <tc:out value="#{solarObject.number}" />
          </tc:column>
          <tc:column label="Orbit"  >
            <tc:out value="#{solarObject.orbit}" />
          </tc:column>
          <tc:column label="Distance" align="right" >
            <tc:out value="#{solarObject.distance}" />
          </tc:column>
          <tc:column label="Period" align="right" >
            <tc:out value="#{solarObject.period}" />
          </tc:column>
          <tc:column label="Incl" align="right" >
            <tc:out value="#{solarObject.incl}" />
          </tc:column>
          <tc:column label="Eccen" align="right" >
            <tc:out value="#{solarObject.eccen}" />
          </tc:column>
          <tc:column label="Discoverer" align="right" >
            <tc:out value="#{solarObject.discoverer}" />
          </tc:column>
          <tc:column label="DiscoverYear" align="right" >
            <tc:out value="#{solarObject.discoverYear}" />
          </tc:column>
        </tc:sheet>
    </tc:box>
  </tc:page>
</f:view>
