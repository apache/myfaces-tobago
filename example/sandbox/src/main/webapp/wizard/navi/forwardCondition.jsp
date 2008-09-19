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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:wizard>
  <jsp:body>

    <tcs:wizard var="w" controller="#{controller.wizard}" title="Forward Condition" outcome="forwardCondition">

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;*;fixed"/>
        </f:facet>

        <tcs:wizardTrain wizard="w" controller="#{controller.wizard}" />

        <tc:out value="Forward Condition"/>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="*;fixed;fixed;fixed;fixed"/>
          </f:facet>

          <tc:cell/>
          <tcs:wizardPrevious wizard="w" label="Vorherige"/>
          <tcs:wizardNext wizard="w" label="Nächste" action="finish"/>
          <tcs:wizardLeave wizard="w" label="Fertig"/>
          <tcs:wizardLeave wizard="w" label="Cancel"/>

        </tc:panel>

      </tc:panel>

    </tcs:wizard>

  </jsp:body>
</layout:wizard>
