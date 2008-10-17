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

<%@ taglib uri="http://myfaces.apache.org/tobago/sandbox" prefix="tcs" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:wizard>
  <jsp:body>

    <tcs:wizard var="w" controller="#{controller.wizard}" title="File Into Condition" outcome="fileIntoCondition">
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;*;fixed"/>
        </f:facet>

        <tcs:wizardTrain wizard="w" controller="#{controller.wizard}" />

        <tc:out value="File Into Condition"/>

        <tx:selectOneChoice label="Header Field">
          <tc:selectItem itemLabel="To:" itemValue="to" />
          <tc:selectItem itemLabel="From:" itemValue="from"/>
          <tc:selectItem itemLabel="Subject:" itemValue="subject"/>
        </tx:selectOneChoice>

        <tx:selectOneChoice label="Condition">
          <tc:selectItem itemLabel="contains" itemValue="contains"/>
          <tc:selectItem itemLabel="equals" itemValue="equals"/>
        </tx:selectOneChoice>

        <tx:in label="Value" value="#{controller.filterValue}"/>

        <tx:in label="Folder"/>

        <tc:cell/>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="*;fixed;fixed;fixed;fixed"/>
          </f:facet>

          <tc:cell/>
          <tcs:wizardPrevious wizard="w" label="Vorherige"/>
          <tcs:wizardNext wizard="w" label="Nächste" action="finish"/>
          <tcs:wizardFinish wizard="w" label="Fertig"/>
          <tcs:wizardCancel wizard="w" action="index" label="Cancel"/>

        </tc:panel>

      </tc:panel>
    </tcs:wizard>

  </jsp:body>
</layout:wizard>
