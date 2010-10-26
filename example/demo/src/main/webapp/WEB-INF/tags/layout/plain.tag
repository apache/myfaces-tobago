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

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>

<f:view locale="#{clientConfigController.locale}">
  <tc:loadBundle basename="overview" var="overviewBundle"/>
  <tc:page applicationIcon="icon/favicon.ico" label="#{overviewBundle.pageTitle}" id="page" width="1000px" height="750px">

    <f:facet name="layout">
      <tc:gridLayout border="0" columns="*;4*"
        margin="10px" />
    </f:facet>

    <tc:cell>
      <f:subview id="content" >
        <jsp:doBody/>
      </f:subview>
    </tc:cell>

  </tc:page>
</f:view>
