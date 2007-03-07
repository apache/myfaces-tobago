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

<%@ page import="org.apache.myfaces.tobago.util.VariableResolverUtil"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ page import="javax.servlet.jsp.jstl.core.LoopTagStatus" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  // load bean before expression in foreach
  VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), "birdList");
%>
<f:view>
  <tc:page height="200px" width="200px">
    <f:facet name="layout">
      <tc:gridLayout />
    </f:facet>
    <c:forEach items="${birdList.birds}" varStatus="status" >
       <% String value = "#{birdList.birds[ " +
           ((LoopTagStatus) pageContext.getAttribute("status")).getIndex() +"]}"; %>
      <tc:out value="<%=value%>" id="out${status.index}"/>
    </c:forEach>
  </tc:page>
</f:view>