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
tc:cell/>--%>

<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="selectoneListbox">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="90px;90px;1*" columns="300px;1*" />
        </f:facet>
<%-- code-sniplet-start id="selectOneListbox" --%>
        <tx:selectOneListbox id="LabeledInlineSingleSelect"
                             label="Contact via: " height="90px">
          <f:selectItem itemValue="Phone" itemLabel="Phone" />
          <f:selectItem itemValue="eMail" itemLabel="eMail"/>
          <f:selectItem itemValue="Mobile" itemLabel="Mobile"/>
          <f:selectItem itemValue="Fax"  itemLabel="Faxscimile"/>
        </tx:selectOneListbox>
        <tc:cell/>
        <tx:selectOneListbox id="LabeledInlineSingleSelect1"
                             label="Contact via: " height="90px">
          <f:selectItem itemValue="Phone" itemLabel="Phone" />
          <f:selectItem itemValue="eMail" itemLabel="eMail"/>
          <f:selectItem itemValue="Mobile" itemLabel="Mobile"/>
          <f:selectItem itemValue="Fax"  itemLabel="Faxscimile"/>
           <f:facet name="click">
            <tc:command />
          </f:facet>
        </tx:selectOneListbox>
<%-- code-sniplet-end id="selectOneListbox" --%>
        <tc:cell/>
        <tc:cell/>
        <tc:cell/>

      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>