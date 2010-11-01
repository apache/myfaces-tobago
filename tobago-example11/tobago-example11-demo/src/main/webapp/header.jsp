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

<f:subview id="header">
  <tc:panel>
    <f:facet name="layout">
      <tc:gridLayout columns="150px;*;200px"/>
    </f:facet>

    <tc:link action="#{navigation.gotoFirst}" immediate="true" image="image/tobago_head.gif"/>

    <tc:cell/>

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="fixed;fixed;fixed;*"/>
      </f:facet>
      <tc:out value="Theme: #{clientConfigController.localizedTheme}"/>
      <tc:out value="Locale: #{clientConfigController.localizedLocale}"/>
      <tc:out value="PDL: JSP/JSPX"/>
      <tc:cell/>
    </tc:panel>
  </tc:panel>
</f:subview>
