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
  <tc:page width="600" height="400">
     <f:facet name="layout">
        <tc:gridLayout columns="*" rows="*"/>
      </f:facet>
    <tc:box label="File upload">
    <%--tc:panel--%>
      <f:facet name="layout">
        <tc:gridLayout  rows="20px;208px;20px"  columns="150px;440px"/>
      </f:facet>
      <tc:out value="Hello World"/>
      <tc:file value="#{test.file}" required="true" >
        <tc:validateFileItem contentType="text/*" />
        <tc:validateFileItem maxSize="299" />
      </tc:file>
      <%--tc:messages/--%>
      <tc:cell spanX="2"/>
      <tc:cell spanX="2">
        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="*;auto;*" rows="20px"/>
          </f:facet>
          <tc:cell />
          <tc:button label="Submit" defaultCommand="true" />
          <tc:cell />
        </tc:panel>
      </tc:cell>
    </tc:box>
    <%--/tc:panel--%>
  </tc:page>
</f:view>
