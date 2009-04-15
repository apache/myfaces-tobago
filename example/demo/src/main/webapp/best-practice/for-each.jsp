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

<%@ page import="org.apache.myfaces.tobago.example.demo.bestpractice.BestPracticeController" %>
<%@ page import="org.apache.myfaces.tobago.util.VariableResolverUtil" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.servlet.jsp.jstl.core.LoopTagStatus" %>

<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%
  // load bean before expression in foreach
  BestPracticeController controller
      = (BestPracticeController) VariableResolverUtil.resolveVariable(
      FacesContext.getCurrentInstance(), "bestPracticeController");
%>
<%!
  private String expression(String list, String status, String field, PageContext pageContext) {
    int index = ((LoopTagStatus) pageContext.getAttribute(status)).getIndex();
    return "#{" + list + "[" + index + "]." + field + "}";
  }
  private String fixed(int count) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < count; i++) {
      buffer.append("fixed;");
    }
    return buffer.toString();
  }
%>
<f:view locale="#{clientConfigController.locale}">
  <tc:loadBundle basename="overview" var="overviewBundle"/>
  <tc:page applicationIcon="icon/favicon.ico" id="page" width="600px" height="400px">
    <tc:box label="Best Practice - For Each">
      <f:facet name="layout">
        <tc:gridLayout rows="<%= "100px;" + fixed(controller.getBirds().size()) + "100px;*"%>" columns="2*;*"/>
      </f:facet>

      <tc:cell spanX="2">
        <tc:out escape="false"
                value="Is is possible to use the c:forEach tag of the JSTL to iterate over a list. <br/><b>Warning:</b> <br/>This example is a workaround. <br/>It is not compatible with further releases! <br/>This example works not with JSP-Tag-Files!"/>
      </tc:cell>

      <c:forEach items="${bestPracticeController.birds}" varStatus="status">
        <%
          String label = expression("bestPracticeController.birds", "status", "name", pageContext);
          String value = expression("bestPracticeController.birds", "status", "size", pageContext);
          String action = expression("bestPracticeController.birds", "status", "select", pageContext);
        %>
        <tx:in value="<%=value%>" label="<%=label%>"/>
        <tc:button label="Select" action="<%=action%>"/>
      </c:forEach>

      <tc:cell spanX="2">
        <tc:out value="#{bestPracticeController.status}"/>
      </tc:cell>

      <tc:cell spanX="2"/>

    </tc:box>
  </tc:page>
</f:view>
