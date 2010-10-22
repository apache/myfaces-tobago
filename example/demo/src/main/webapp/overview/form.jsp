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
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout />
      </f:facet>

  <%--
      <tc:out escape="false" value="#{overviewBundle.form_text1}" />
  --%>

      <tc:box label="#{overviewBundle.form_outTitle}">
        <f:facet name="layout">
          <tc:gridLayout rows="80px;fixed;fixed;fixed" />
        </f:facet>

        <tc:out escape="false" value="#{overviewBundle.form_text2}" />

        <tc:box label="#{overviewBundle.form_in1Title}">
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed;" columns="1*;100px"  />
          </f:facet>

          <tc:form id="form1" >
            <tx:in value="#{demo.text[0]}" required="true" label="#{overviewBundle.newValue1}" />
            <tc:cell spanY="2" >
              <tc:button label="#{overviewBundle.submitForm1}" />
            </tc:cell>
            <tx:in value="#{demo.text[0]}" readonly="true" label="#{overviewBundle.modelValue1}" />
          </tc:form>

        </tc:box>

        <tc:box label="#{overviewBundle.form_in2Title}">
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed" columns="1*;100px"/>
          </f:facet>

          <tc:form id="form2">
            <tx:in value="#{demo.text[1]}" required="true" label="#{overviewBundle.newValue2}" />
            <tc:cell spanY="2" >
               <tc:button label="#{overviewBundle.submitForm2}" />
            </tc:cell>
            <tx:in value="#{demo.text[1]}" readonly="true" label="#{overviewBundle.modelValue2}" />
          </tc:form>
        </tc:box>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="*;fixed" rows="fixed;*"  />
          </f:facet>

          <tx:in value="#{demo.text[2]}" required="true" label="#{overviewBundle.newValue}" />
          <tc:cell spanY="2" >
            <tc:button label="#{overviewBundle.submitAll}" />
          </tc:cell>
          <tx:in value="#{demo.text[2]}" readonly="true" label="#{overviewBundle.modelValue}" />

        </tc:panel>
      </tc:box>
    </tc:panel>
  </jsp:body>
</layout:overview>
