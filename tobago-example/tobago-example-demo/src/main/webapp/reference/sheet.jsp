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
    <tc:box label="Sheet">
      <f:facet name="layout">
        <tc:gridLayout rows="auto;*;auto;auto;auto"/>
      </f:facet>

      <tc:messages/>

      <tc:sheet value="#{simpleList}" columns="auto;*" var="bean" rows="5">
        <tc:columnSelector/>
        <tc:column label="Number">
          <tc:in value="#{bean.value}" required="true"/>
        </tc:column>
      </tc:sheet>

      <tc:separator>
        <f:facet name="label">
          <tc:label value="Layout: Sheet with 'auto' height"/>
        </f:facet>
      </tc:separator>

      <tc:sheet value="#{simpleList}" columns="*;*;*" var="bean" rows="5">
        <tc:column label="Cipher">
          <tc:out value="#{bean.number}"/>
        </tc:column>
        <tc:column label="Name">
          <tc:out value="#{bean.value}"/>
        </tc:column>
        <tc:column label="Date">
          <tc:out value="#{bean.date}">
            <f:convertDateTime dateStyle="medium" type="date"/>
          </tc:out>
        </tc:column>
      </tc:sheet>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="*;auto"/>
        </f:facet>
        <tc:cell/>
        <tc:button action="submit" label="Submit"/>
      </tc:panel>

    </tc:box>

  </jsp:body>
</layout:overview>
