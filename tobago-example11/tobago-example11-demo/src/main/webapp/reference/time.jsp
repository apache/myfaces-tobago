<%@ page import="java.text.SimpleDateFormat" %>
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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%
  request.setAttribute("now", new SimpleDateFormat("yyyy-MM-dd").parse("1980-03-22"));
%>

<layout:overview>
  <jsp:body>
      <tc:box label="Time Specific Controls">
        <f:facet name="layout">
          <tc:gridLayout columns="400px;*" rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;250px;*"/>
        </f:facet>
        <%-- code-sniplet-start id="date" --%>
        <tx:date label="Date" value="#{now}">
          <f:convertDateTime pattern="dd.MM.yyyy"/>
        </tx:date>
        <%-- code-sniplet-end id="date" --%>
        <tc:cell/>

        <%-- code-sniplet-start id="dateTime" --%>
        <tx:date label="Date/Time" value="#{now}">
          <f:convertDateTime pattern="dd.MM.yyyy HH:mm"/>
        </tx:date>
        <%-- code-sniplet-end id="dateTime" --%>
        <tc:cell/>

        <tx:date label="Date (focus)" focus="true">
          <f:convertDateTime pattern="dd.MM.yyyy"/>
        </tx:date>
        <tc:cell/>

        <tx:date label="Short style" value="#{now}">
          <f:convertDateTime dateStyle="short" type="both" timeStyle="short"/>
        </tx:date>
        <tc:cell/>

        <tx:date label="Medium Style" value="#{now}">
          <f:convertDateTime dateStyle="medium" type="both" timeStyle="medium" />
        </tx:date>
        <tc:cell/>

        <tx:date label="Long Style" value="#{now}">
          <f:convertDateTime dateStyle="long" type="both" timeStyle="long" />
        </tx:date>
        <tc:cell/>

        <tx:date label="Full Style" value="#{now}">
          <f:convertDateTime dateStyle="full" type="both" timeStyle="full" />
        </tx:date>
        <tc:cell/>

        <tx:date label="Month" value="#{now}">
          <f:convertDateTime pattern="MM/yyyy"/>
        </tx:date>
        <tc:cell/>

        <tx:date label="Year" value="#{now}">
          <f:convertDateTime pattern="yyyy"/>
        </tx:date>
        <tc:cell/>

        <tx:time label="Time" value="#{now}">
          <f:convertDateTime type="time"  pattern="HH:mm"/>
        </tx:time>
        <tc:cell/>

        <tx:time label="Time (Seconds)" value="#{now}">
          <f:convertDateTime type="time" pattern="HH:mm:ss"/>
        </tx:time>
        <tc:cell/>

        <%-- code-sniplet-start id="calendar" --%>
        <tc:calendar />
        <%-- code-sniplet-end id="calendar" --%>
        <tc:cell/>

        <tc:cell spanX="2"/>

      </tc:box>
  </jsp:body>
</layout:overview>
