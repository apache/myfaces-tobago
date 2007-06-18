<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
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
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%!
  public static String exceptionToString(Throwable exception) {
    if (exception == null) {
      return "No Exception available!";
    }
    StringBuilder builder = new StringBuilder();
    builder.append("<pre>");
    StringWriter stringWriter = new StringWriter();
    exception.printStackTrace(new PrintWriter(stringWriter));
    builder.append(stringWriter.toString());
    builder.append("</pre>");
    return builder.toString();
  }
%>
<%
  request.setAttribute("exceptionText", exceptionToString(exception));
%>

<layout:overview>
  <jsp:body>
    <tc:box label="Sorry, an error has occured!">
      <f:facet name="layout">
        <tc:gridLayout rows="fixed;*" />
      </f:facet>

      <tc:messages />
      <tc:cell scrollbars="auto">
        <tc:out value="#{exceptionText}" escape="false"/>
      </tc:cell>
    </tc:box>
  </jsp:body>
</layout:overview>
