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
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
      <tc:box label="Non Faces Response (e. g. a download link)">
        <f:facet name="layout">
          <tc:gridLayout rows="35px;35px;*" columns="*;100px" />
        </f:facet>

        <tc:out value="A faces request to a non-faces-response. Inside of the Browser." />
        <tc:button action="#{bestPracticeController.viewPdfInBrowser}" label="View Pdf" />
        <tc:out value="A faces request to a non-faces-response. Outside of the Browser." />
        <tc:button action="#{bestPracticeController.viewPdfOutsideOfBrowser}" label="View Pdf" transition="false" />
        <tc:cell />
        <tc:cell />

      </tc:box>
  </jsp:body>
</layout:overview>
