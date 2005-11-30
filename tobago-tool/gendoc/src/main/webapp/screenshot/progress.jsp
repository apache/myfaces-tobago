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
<%@ page
    errorPage="/errorPage.jsp"
    import="javax.swing.DefaultBoundedRangeModel"
    %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%-- Progress --%>

<%
  DefaultBoundedRangeModel progress = new DefaultBoundedRangeModel(75, 0, 0, 100);
  pageContext.setAttribute("progress", progress, PageContext.REQUEST_SCOPE);
%>

<layout:screenshot>
  <f:subview id="progress">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*" columns="100px;1* "/>
        </f:facet>
          <tc:label value="Progress: " inline="true"/>
          <%-- code-sniplet-start id="progress" --%>
          <tc:progress value="#{progress}"/>
          <%-- code-sniplet-end id="progress" --%>
        <tc:cell/>
      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>