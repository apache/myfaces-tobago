<%--
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="130px;120px;1*"  />
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.locale_text}" />

      <tc:box label="#{overviewBundle.locale_title}" height="150px">
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*" />
        </f:facet>

        <tx:selectOneChoice value="#{clientConfigController2.locale}"
            label="#{overviewBundle.locale}">
          <f:selectItems value="#{clientConfigController2.localeItems}" />
        </tx:selectOneChoice>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="100px;*;100px" rows="1*;fixed"/>
          </f:facet>
          <tc:cell spanY="2" >
            <tc:image value="image/country.gif" width="100px" height="100px" />
          </tc:cell>

          <tc:cell spanY="2"/>

          <tc:cell/>
          <tc:button action="#{clientConfigController2.submit}" label="#{overviewBundle.locale_submit}" />
        </tc:panel>

      </tc:box>
      <tc:cell/>
    </tc:panel>
  </jsp:body>
</layout:overview>
