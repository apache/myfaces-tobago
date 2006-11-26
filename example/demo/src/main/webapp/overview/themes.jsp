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
    <tc:panel>
      <f:facet name="layout">
          <tc:gridLayout rows="120px;150px;1* " />
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.theme_text}" />

      <tc:box label="#{overviewBundle.theme_title}" >
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*;fixed" />
        </f:facet>

        <tx:selectOneChoice value="#{clientConfigController2.theme}"
            label="#{overviewBundle.theme_label}">
          <f:selectItems value="#{clientConfigController2.themeItems}" />
        </tx:selectOneChoice>

        <tc:cell/>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="*;100px"   />
          </f:facet>
          <tc:cell/>
          <tc:button action="#{clientConfigController2.submit}" label="#{overviewBundle.theme_submit}" />
        </tc:panel>

      </tc:box>
      <tc:cell />
    </tc:panel>
  </jsp:body>
</layout:overview>
