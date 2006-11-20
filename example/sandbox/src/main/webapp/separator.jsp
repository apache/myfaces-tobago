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

<%--
  User: bommel
  Date: Nov 20, 2006
  Time: 9:29:45 AM
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/sandbox" prefix="tcs" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <tc:loadBundle basename="demo" var="bundle"/>

  <tc:page label="Sandbox - Separator" width="500px" height="800px">
    <f:facet name="layout">
      <tc:gridLayout margin="10px" rows="*"/>
    </f:facet>
    <tc:box label="Separator">
      <f:facet name="layout">
        <tc:gridLayout columns="1*;1*" rows="fixed;fixed;*"/>
      </f:facet>
      <tc:cell/>
      <tc:cell/>
      <tcs:separator />
      <tcs:separator>
        <f:facet name="label">
          <tc:label value="Test" />
        </f:facet>
      </tcs:separator>
      <tc:cell/>
      <tc:cell/>
    </tc:box>
  </tc:page>
</f:view>