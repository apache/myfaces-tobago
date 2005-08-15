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

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="125px;1*;115px"/>
      </f:facet>

      <t:out escape="false" value="#{miniHowtoBundle.jspDefinitionText1}"/>
      <t:box label="#{miniHowtoBundle.jspDefinitionCodeExampleBoxTitle1}">
        <t:out value="#{miniHowtoBundle.jspDefinitionCodeExample1}"/>
      </t:box>
      <t:out escape="false" value="#{miniHowtoBundle.jspDefinitionText2}"/>
    </t:panel>
  </jsp:body>
</layout:mini-howto>
