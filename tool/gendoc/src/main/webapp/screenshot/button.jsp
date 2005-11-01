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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <jsp:body>
    <f:subview id="button">
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="100px;1*" rows="fixed;1*" />
        </f:facet>
<%-- code-sniplet-start id="button" --%>
        <tc:button label="Save" />
<%-- code-sniplet-end id="button" --%>
        <tc:cell/>

        <tc:cell spanX="2"/>

      </tc:panel>

    </f:subview>
  </jsp:body>
</layout:screenshot>