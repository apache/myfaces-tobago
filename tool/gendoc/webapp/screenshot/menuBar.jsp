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

<layout:screenshot>
  <f:subview id="menu">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" columns="400px;1*"/>
        </f:facet>
        <%-- code-sniplet-start id="menuBar" --%>
        <t:menuBar>
          <t:menu labelWithAccessKey="_File">
            <t:menuItem label="New File"/>
            <t:menuItem label="Open File"/>
            <t:menuItem label="Save"/>
            <t:menuItem label="Print"/>
            <t:menuSeparator/>
            <t:menuItem label="Exit"/>
            <t:menucheck label="Administration Mode"/>
          </t:menu>

          <%-- code-sniplet-start id="menu" --%>
          <t:menu labelWithAccessKey="_Edit">
            <t:menuItem label="Copy"/>
            <t:menuItem label="Cut"/>
            <t:menuItem label="Paste"/>
            <%-- code-sniplet-start id="menuItem" --%>
            <t:menuItem label="Delete"/>
            <%-- code-sniplet-end id="menuItem" --%>
            <t:menuSeparator/>
            <t:menu labelWithAccessKey="_Delete">
              <t:menuItem label="As Spam"/>
              <t:menuItem label="As Newsletter"/>
              <t:menuItem label="As Uninteresting"/>
            </t:menu>
          </t:menu>
          <%-- code-sniplet-end id="menu" --%>

          <t:menu labelWithAccessKey="_View">
            <t:menuItem label="Snap to Grid"/>
            <t:menuItem label="Show Rulers"/>
          </t:menu>
        </t:menuBar>
        <%-- code-sniplet-end id="menuBar" --%>
        <t:cell/>
        <t:cell/>
        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>