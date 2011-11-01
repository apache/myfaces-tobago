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
    <tc:box label="InputSuggest">
      <f:facet name="layout">
        <tc:gridLayout rows="auto;50px;auto;auto;50px;150px;auto;1*"/>
      </f:facet>

      <tc:separator label="Simple"/>

      <tc:out value=" suggestion starts with the 2. character and just adds an index to the prefix"/>

      <tc:panel >
        <f:facet name="layout">
          <tc:gridLayout columns="350px;1*"/>
        </f:facet>

        <tx:in value="#{inputSuggestController.simpleValue}"
               label="Suggest test:"
               tip="test"
               suggestMethod="#{inputSuggestController.getSimpleSuggestItems}"/>

        <tc:cell/>

      </tc:panel>

      <tc:separator label="Multi field suggest"/>

      <tc:out value="Try \"26...\" in the ZIP, or \"ol...\" in the city field. On selection ZIP, City and State are updated, focus goes to textarea. "/>      

      <tc:panel >
        <f:facet name="layout">
          <tc:gridLayout rows="auto;auto;1*" columns="350px;1*"/>
        </f:facet>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="200px;1*"/>
          </f:facet>
          <tx:in id="isZip" value="#{inputSuggestController.zipValue}"
                 label="ZIP/City:"
                 tip="test"
                 suggestMethod="#{inputSuggestController.getZipSuggestItems}"/>
          <tc:in id="isCity" value="#{inputSuggestController.cityValue}"
                 tip="test"
                 suggestMethod="#{inputSuggestController.getCitySuggestItems}"/>
        </tc:panel>

        <tc:cell spanY="3"/>

        <tx:selectOneChoice label="State:"
                            id="isState"
            value="#{inputSuggestController.region}">
          <f:selectItems value="#{inputSuggestController.regionItems}"/>
        </tx:selectOneChoice>

        <tc:textarea id="txarea"/>


      </tc:panel>


      <tc:separator/>

      <tc:cell/>

    </tc:box>
  </jsp:body>
</layout:overview>
