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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="selectManyListbox">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="40px;1*" />
        </f:facet>
<%-- code-sniplet-start id="selectManyListbox" --%>
        <tc:selectManyListbox inline="true" id="LabeledInlineMultiSelect"
                             labelWithAccessKey="Contact via: " height="90px">
          <f:selectItem itemValue="Phone" itemLabel="Phone" />
          <f:selectItem itemValue="eMail" itemLabel="eMail"/>
          <f:selectItem itemValue="Mobile" itemLabel="Mobile"/>
          <f:selectItem itemValue="Fax"  itemLabel="Faxscimile"/>
        </tc:selectManyListbox>
<%-- code-sniplet-end id="selectManyListbox" --%>
        <tc:cell/>

      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>