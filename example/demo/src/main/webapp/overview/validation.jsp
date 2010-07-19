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
        <tc:gridLayout rows="80px;200px;1*;" />
      </f:facet>

      <tc:out escape="false" value="#{overviewBundle.validation_text}" />

      <tc:box label="#{overviewBundle.validation_sampleTitle}" >
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*;fixed;fixed" />
        </f:facet>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="1*;1*" />
          </f:facet>
          <tx:in label="#{overviewBundle.validation_number}" markup="number" required="true">
            <f:validateLength minimum="7" maximum="7" />
            <f:validateLongRange maximum="9000000"/>
          </tx:in>
          <tx:in label="#{overviewBundle.validation_price}" markup="number">
            <f:validateDoubleRange minimum="0.01" maximum="1000" />
          </tx:in>
           <tx:in label="#{overviewBundle.validation_custom}" validator="#{overviewController.customValidator}" >
          </tx:in>
        </tc:panel>

        <tx:textarea label="#{overviewBundle.validation_description}"
            required="true"  />

        <tc:messages />

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="1*;100px"   />
          </f:facet>
          <tc:cell/>
          <tc:button action="#{clientConfigController.submit}"
              label="#{overviewBundle.validation_submit}" />
        </tc:panel>

      </tc:box>
      <tc:cell />
    </tc:panel>
  </jsp:body>
</layout:overview>
