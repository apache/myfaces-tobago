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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view locale="#{clientConfigController.locale}"
    ><tc:loadBundle basename="overview" var="overviewBundle" 
  /><tc:page label="#{overviewBundle.pageTitle}" id="page"
      width="750px" height="600px">

    <jsp:include page="/overview/menubar.jsp" />
    <%--f:facet name="backButtonDetector">
      <tc:hidden value="#{overviewNavigation.currentRequestTime}"
                 converter="org.apache.myfaces.tobago.converter.BackButtonDetection" >
        <f:validator validatorId="org.apache.myfaces.tobago.validator.BackButtonDetection" />
      </tc:hidden>
    </f:facet--%>
    <f:facet name="layout">
      <tc:gridLayout border="0" columns="1*;4*"
        margin="10px" rows="100px;*;fixed"  />
    </f:facet>

    <tc:cell spanX="2">
      <jsp:include page="/overview/header.jsp"/>
    </tc:cell>

    <tc:cell spanY="2" >
      <jsp:include page="/overview/navigator.jsp"/>
    </tc:cell>

    <tc:cell>
      <jsp:doBody/>
    </tc:cell>

    <tc:cell>
      <jsp:include page="/overview/footer.jsp" />
    </tc:cell>

  </tc:page>
</f:view>
