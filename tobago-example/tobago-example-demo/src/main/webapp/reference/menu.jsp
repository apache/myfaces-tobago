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
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>

<layout:overview>
  <jsp:body>
    <tc:box label="Menu">
      <f:facet name="layout">
        <tc:gridLayout rows="auto;*"/>
      </f:facet>
      <%-- code-sniplet-start id="menuBar" --%>
      <f:facet name="menuBar">
        <tc:menuBar>
          <tc:menu label="_File">
            <tc:menuCommand label="New File"/>
            <tc:menuCommand label="Open File"/>
            <tc:menuCommand label="Save"/>
            <tc:menuCommand label="Print"/>
            <tc:menuSeparator/>
            <tc:menuCommand label="Exit">
              <f:facet name="confirmation">
                <tc:out value="Are you sure?"/>
              </f:facet>
            </tc:menuCommand>
            <%--<tx:menuCheckbox action="none" label="Administration Mode"/>--%>
          </tc:menu>

          <%-- code-sniplet-start id="menu" --%>
          <tc:menu label="_Edit">
            <tc:menuCommand label="Copy"/>
            <tc:menuCommand label="Cut"/>
            <tc:menuCommand label="Paste"/>
            <%-- code-sniplet-start id="menuCommand" --%>
            <tc:menuCommand label="Delete"/>
            <%-- code-sniplet-end id="menuCommand" --%>
            <tc:menuSeparator/>
            <tc:menu label="_Delete">
              <tc:menuCommand label="As Spam"/>
              <tc:menuCommand label="As Newsletter"/>
              <tc:menuCommand label="As Uninteresting"/>
            </tc:menu>
          </tc:menu>
          <%-- code-sniplet-end id="menu" --%>

          <tc:menu label="_View">
            <tc:menuCommand label="Snap to Grid"/>
            <tc:menuCommand label="Show Rulers"/>
          </tc:menu>
        </tc:menuBar>
      </f:facet>
      <%-- code-sniplet-end id="menuBar" --%>
    </tc:box>
  </jsp:body>
</layout:overview>
