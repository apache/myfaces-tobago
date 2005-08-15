<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="80px;200px;1*;" />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.validation_text}" />

      <t:box label="#{overviewBundle.validation_sampleTitle}" >
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*;fixed;fixed" />
        </f:facet>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="1*;1*" />
          </f:facet>
          <t:in label="#{overviewBundle.validation_number}"
              required="true">
            <f:validateLength minimum="7" maximum="7" />
            <f:validateLongRange />
          </t:in>
          <t:in label="#{overviewBundle.validation_price}">
            <f:validateDoubleRange minimum="0.01" maximum="1000" />
          </t:in>
        </t:panel>

        <t:textarea label="#{overviewBundle.validation_description}"
            required="true"  />

        <t:messages />

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="1*;100px"   />
          </f:facet>
          <t:cell/>
          <t:button action="#{clientConfigController.submit}"
              label="#{overviewBundle.validation_submit}" />
        </t:panel>

      </t:box>
      <t:cell />
    </t:panel>
  </jsp:body>
</layout:overview>
