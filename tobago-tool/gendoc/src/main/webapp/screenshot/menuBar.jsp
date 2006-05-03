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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="menu">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*" columns="400px;1*"/>
        </f:facet>
        <%-- code-sniplet-start id="menuBar" --%>
        <tc:menuBar>
          <tc:menu label="_File">
            <tc:menuItem label="New File"/>
            <tc:menuItem label="Open File"/>
            <tc:menuItem label="Save"/>
            <tc:menuItem label="Print"/>
            <tc:menuSeparator/>
            <tc:menuItem label="Exit"/>
            <%--<tc:menuCheckbox action="none" label="Administration Mode"/>--%>
          </tc:menu>

          <%-- code-sniplet-start id="menu" --%>
          <tc:menu label="_Edit">
            <tc:menuItem label="Copy"/>
            <tc:menuItem label="Cut"/>
            <tc:menuItem label="Paste"/>
            <%-- code-sniplet-start id="menuItem" --%>
            <tc:menuItem label="Delete"/>
            <%-- code-sniplet-end id="menuItem" --%>
            <tc:menuSeparator/>
            <tc:menu label="_Delete">
              <tc:menuItem label="As Spam"/>
              <tc:menuItem label="As Newsletter"/>
              <tc:menuItem label="As Uninteresting"/>
            </tc:menu>
          </tc:menu>
          <%-- code-sniplet-end id="menu" --%>

          <tc:menu label="_View">
            <tc:menuItem label="Snap to Grid"/>
            <tc:menuItem label="Show Rulers"/>
          </tc:menu>
        </tc:menuBar>
        <%-- code-sniplet-end id="menuBar" --%>
        <tc:cell/>
        <tc:cell/>
        <tc:cell/>

      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>