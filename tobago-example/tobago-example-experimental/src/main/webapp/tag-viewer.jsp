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
<%@ taglib uri="http://myfaces.apache.org/tobago/example/demo" prefix="demo" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <tc:page applicationIcon="icon/favicon.ico" label="Tag Viewer" id="page"
           width="1000px" height="500px">
    <f:facet name="layout">
      <tc:gridLayout columns="*;*;2*" margin="5px"/>
    </f:facet>

    <tc:box label="Tag">
      <f:facet name="layout">
        <tc:gridLayout rows="*" columns="*"/>
      </f:facet>
      <tc:sheet var="tag" columns="*" value="#{reference.tags}">
        <tc:column label="Name">
          <tc:out value="#{tag.name}" tip="#{tag.tip}"/>
        </tc:column>
      </tc:sheet>
    </tc:box>
    <tc:box label="Attribute">
      <f:facet name="layout">
        <tc:gridLayout rows="*" columns="*"/>
      </f:facet>
      <tc:sheet var="attribute" columns="*;*" value="#{reference.attributes}">
        <tc:column label="Name">
          <tc:out value="#{attribute.name}"/>
        </tc:column>
        <tc:column label="Value">
          <tc:out value="#{attribute.name}"/>
        </tc:column>
      </tc:sheet>
    </tc:box>
  </tc:page>
</f:view>
