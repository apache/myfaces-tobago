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
<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="viewJspButton_jsp" ><%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  String contextPath = facesContext.getExternalContext().getRequestContextPath();
  String sourceName = request.getRequestURI().substring(contextPath.length());
  String action = "/viewSource.jsp?jsp=" + sourceName;
%>
  <tc:button id="viewJspButton" action="<%= action %>" type="navigate"
      label="JSP" image="image/source.gif" />
</f:subview>
