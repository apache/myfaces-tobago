<%--
 * Copyright 2002-2005 atanion GmbH.
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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="130px;120px;1*"  />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.locale_text}" />

      <t:box label="#{overviewBundle.locale_title}" height="150px">
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" />
        </f:facet>

        <t:selectOneChoice value="#{clientConfigController2.locale}"
            label="#{overviewBundle.locale}">
          <f:selectItems value="#{clientConfigController2.localeItems}" />
        </t:selectOneChoice>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="100px;*;100px" rows="1*;fixed"/>
          </f:facet>
          <t:cell spanY="2" >
            <t:image value="image/country.gif" width="100px" height="100px" />
          </t:cell>

          <t:cell spanY="2"/>

          <t:cell/>
          <t:button action="#{clientConfigController2.submit}" label="#{overviewBundle.locale_submit}" />
        </t:panel>

      </t:box>
      <t:cell/>
    </t:panel>
  </jsp:body>
</layout:overview>
