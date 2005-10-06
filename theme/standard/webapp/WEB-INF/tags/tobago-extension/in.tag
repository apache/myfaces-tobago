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
<%@ taglib uri="http://www.atanion.com/tobago/extension" prefix="tx" %>

<%@ attribute name="binding" %>
<%@ attribute name="converter" %>
<%@ attribute name="disabled" %>
<%@ attribute name="id" %>
<%@ attribute name="label" %>
<%@ attribute name="readonly" %>
<%@ attribute name="rendered" %>
<%@ attribute name="required" %>
<%@ attribute name="tip" %>
<%@ attribute name="value" %>

<tx:label
    tip="${tip}"
    value="${label}">
  <t:in
      binding="${binding}"
      converter="${converter}"
      disabled="${disabled}"
      id="${id}"
      readonly="${readonly}"
      rendered="${rendered}"
      required="${required}"
      tip="${tip}"
      value="${value}">
<%--
  <c:if test="${value != null}">
    <jsp:attribute name="value">${value}</jsp:attribute>
  </c:if>
--%>
 </t:in>
</tx:label>
