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
    <tc:box label="Object">
      <f:facet name="layout">
        <tc:gridLayout columns="*" rows="fixed;*"/>
      </f:facet>
      <tc:link label="Show Trinidad and Tobago"
         link="http://maps.google.com/maps?f=q&hl=en&q=Tobago+Trinidad+and+Tobago&ie=UTF8&cd=1&geocode=0,11.219535,-60.669460&t=h&om=1&s=AARTsJqqwZcE2iAOIG6P8y5bl0cr1jRrAA&ll=11.257541,-60.652771&spn=0.117851,0.145912&z=12&iwloc=addr&output=embed" target="page:map"/>
      <tc:object id="map" />
    </tc:box>
  </jsp:body>
</layout:overview>