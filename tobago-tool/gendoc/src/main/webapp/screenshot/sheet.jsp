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
<%@ page import="org.apache.myfaces.tobago.example.reference.SimpleBean" %>

<%
  SimpleBean[] simpleList = (SimpleBean[]) session.getAttribute("simpleList");
  System.out.println("1" + session.getAttribute("simpleList"));
  if (simpleList == null) {
    simpleList = new SimpleBean[]{
        new SimpleBean("One"),
        new SimpleBean("Two"),
        new SimpleBean("Three"),
        new SimpleBean("Four")};
  }
  session.setAttribute("simpleList", simpleList);
  System.out.println("2" + session.getAttribute("simpleList"));
%>

<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="tree">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;*;fixed"/>
        </f:facet>

        <tc:messages/>

        <tc:sheet value="#{simpleList}" columns="*" var="bean">
          <tc:column label="Number">
            <tc:in value="#{bean.name}" required="true"/>
          </tc:column>
        </tc:sheet>

        <tc:button label="submit"/>

      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>
