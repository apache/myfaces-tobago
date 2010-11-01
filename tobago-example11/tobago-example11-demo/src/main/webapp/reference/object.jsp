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

<layout:overview>
  <jsp:body>
    <tc:box label="Object - Show external content inside of an application">
      <f:facet name="layout">
        <tc:gridLayout rows="fixed;*"/>
      </f:facet>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="80px;80px;80px;*"/>
        </f:facet>
        <tc:link
            label="Show Tobago"
            link="http://maps.google.com/maps?f=q&ie=UTF8&cd=1&t=h&ll=11.249123,-60.687103&z=10&output=embed"
            target="page:content:map"/>
        <tc:link
            label="Show Plane"
            link="http://maps.google.com/maps?f=q&ie=UTF8&cd=1&t=h&sll=50.053839,8.624933&z=17&output=embed"
            target="page:content:map"/>
        <tc:link
            label="Show World"
            link="http://maps.google.com/maps?f=q&ie=UTF8&cd=1&t=h&ll=0,0&z=1&output=embed"
            target="page:content:map"/>
        <tc:cell/>
      </tc:panel>

      <tc:object id="map"/>

    </tc:box>
  </jsp:body>
</layout:overview>
