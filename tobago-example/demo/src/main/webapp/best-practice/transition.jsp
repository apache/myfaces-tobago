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

<layout:overview>
  <jsp:body>
    <tc:box label="Transitions between pages">
      <f:facet name="layout">
        <tc:gridLayout rows="fixed;fixed;fixed;*" />
      </f:facet>

      <tc:out value="Prevent double-clicks" />
      <tc:button label="Sleep 5 s (transition=true)" action="#{transitionController.sleep5s}"/>
      <tc:button label="Sleep 5 s (transition=false)" action="#{transitionController.sleep5s}" transition="false"/>

      <tc:cell />

    </tc:box>
  </jsp:body>
</layout:overview>
