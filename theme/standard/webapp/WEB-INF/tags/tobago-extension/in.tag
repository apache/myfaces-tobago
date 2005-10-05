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
<%@ taglib tagdir="/WEB-INF/tags/tobago-extension" prefix="tx" %>

<%@ attribute name="label" %>
<%@ attribute name="value" %>
<%@ attribute name="required" %>

<tx:label value="${label}">
   <t:in value="${value}" required="${required}"/>
</tx:label>

<%--
<t:panel>
  <f:facet name="layout">
    <t:gridLayout columns="100px;*"/>
  </f:facet>
  <t:label value="${label}" />
  <t:in value="${value}" required="${required}"/>
  <!--<u:labelSolver  />-->
</t:panel>
--%>
