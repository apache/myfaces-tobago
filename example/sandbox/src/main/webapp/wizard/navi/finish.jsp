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

    <tcs:wizard var="w" controller="#{controller.wizard}" title="Finish" outcome="finish">
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;*;fixed"/>
        </f:facet>

        <tcs:wizardTrain wizard="w" controller="#{controller.wizard}" />

        <tc:out value="Click finish for activation."/>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="*;fixed;fixed;fixed;fixed"/>
          </f:facet>

          <tc:cell/>
          <tcs:wizardPrevious wizard="w" label="Vorherige"/>
          <tcs:wizardNext wizard="w" label="Nächste"/>
          <tcs:wizardFinish wizard="w" label="Fertig" action="index"/>
          <tcs:wizardCancel wizard="w" label="Cancel"/>

        </tc:panel>

      </tc:panel>

    </tcs:wizard>

  </jsp:body>
</layout:wizard>
<%--
<layout:wizard>
  <jsp:body>

    <tcs:wizard controller="#{controller.wizard}" title="File Into Condition" outcome="fileIntoCondition" next="finish"></tcs:wizard>
    <tcs:wizard controller="#{controller.wizard}" title="New Filter" outcome="filter" next="#{controller.createFilter}"></tcs:wizard>

    <tcs:wizardStep controller="#{controller.wizard}" var="w" title="Finish" outcome="finish">


    <tcs:wizardTrain controller="#{controller.wizard}" title="Finish" outcome="finish" finish="index" />

      <tc:out value="Click finish for activation."/>

    <tcs:wizardPrevious controller="#{controller.wizard}" title="Finish" outcome="finish" finish="index" />
    <tcs:wizardNext controller="#{controller.wizard}" title="Finish" outcome="finish" finish="index" />
    <tcs:wizardFinish controller="#{controller.wizard}" title="Finish" outcome="finish" finish="index" />
    <tcs:wizardCancel controller="#{controller.wizard}" title="Finish" outcome="finish" finish="index" />

    </tcs:wizardStep>

  </jsp:body>
</layout:wizard>
--%>