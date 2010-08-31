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

<layout:overview>
  <jsp:body>
    <tc:box label="Server Info">
      <f:facet name="layout">
        <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;*" />
      </f:facet>

      <tx:in value="#{info.version}" readonly="true"
          label="Tobago Version" />

      <tx:in value="#{info.jsfTitle}" readonly="true"
          label="JSF Implementation" />

      <tx:in value="#{info.jsfVersion}" readonly="true"
          label="JSF Version" />

      <tx:in value="#{info.serverInfo}" readonly="true"
          label="Server Info" />

      <tx:in value="#{info.systemProperties['java.runtime.version']} - #{info.systemProperties['java.vm.vendor']}" readonly="true"
          label="Java" />

      <tx:in value="#{info.systemProperties['os.name']} - #{info.systemProperties['os.version']} - #{info.systemProperties['os.arch']}" readonly="true"
          label="Operating System" />

      <tc:sheet var="entry" value="#{info.systemPropertiesAsList}" columns="*;2*" rows="1000">
        <tc:column label="Key">
          <tc:out value="#{entry.key}"/>
        </tc:column>
        <tc:column label="Value">
          <tc:out value="#{entry.value}" escape="true"/>
        </tc:column>
      </tc:sheet>
      
    </tc:box>
  </jsp:body>
</layout:overview>
