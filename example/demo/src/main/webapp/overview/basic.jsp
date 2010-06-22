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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>

<layout:overview>
  <jsp:body>
    <tc:box label="#{overviewBundle.basic}">
      <%--
      <f:facet name="reload">
        <tc:reload frequency="5000" />
      </f:facet>
      --%>
      <f:facet name="layout">
        <tc:gridLayout border="0" rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed" />
      </f:facet>

      <%-- row --%>
      <tc:separator>
        <f:facet name="label">
          <tc:label value="#{overviewBundle.basic_sampleInputTitle}"/>
        </f:facet>
      </tc:separator>

      <%-- row --%>
      <tc:messages />

      <%-- row --%>
      <tc:out escape="false" value="#{overviewBundle.basic_text_input}" />

      <%-- row --%>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="*;*" border="0" />
        </f:facet>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed;fixed;fixed" />
          </f:facet>
          <tx:in value="#{overviewController.basicInput}" required="true" tabIndex="1"
                 label="#{overviewBundle.basic_textboxLabel}" tip="#{overviewBundle.basic_textboxTip}"/>
          <tx:in value="#{overviewController.suggestInput}" tabIndex="3"
                 label="#{overviewBundle.basic_suggestLabel}" tip="#{overviewBundle.basic_suggestTip}"
                 suggestMethod="#{overviewController.getInputSuggestItems}"/>
          <tx:date value="#{overviewController.basicDate}" tabIndex="4"
                   label="#{overviewBundle.basic_dateLabel}" required="true" >
            <f:convertDateTime pattern="dd/MM/yyyy" />
            <tc:validateSubmittedValueLength maximum="10"/>
          </tx:date>
          <tx:time label="#{overviewBundle.basic_timeLabel}" tabIndex="5"
              value="#{overviewController.basicTime}" />
        </tc:panel>

        <tx:textarea value="#{overviewController.basicArea}" tabIndex="2"
            label="#{overviewBundle.basic_textareaLabel}" >
          <f:validateLength maximum="10" />
        </tx:textarea>
      </tc:panel>

      <%-- row --%>
      <tc:separator>
        <f:facet name="label">
          <tc:label value="#{overviewBundle.basic_sampleSelectTitle}"/>
        </f:facet>
      </tc:separator>

      <%-- row --%>
      <tc:out escape="false" value="#{overviewBundle.basic_text_select}" />

      <%-- row --%>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="*;*"  border="0"/>
        </f:facet>
        <tc:selectOneRadio value="#{overviewController.radioValue}"
                           id="rg0" converter="salutationId">
          <f:selectItems value="#{overviewController.items}" id="items0" />
          <f:facet name="click">
            <tc:command/>
          </f:facet>
        </tc:selectOneRadio>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed"/>
          </f:facet>
          <tc:selectManyCheckbox value="#{overviewController.multiValue}" 
                                 id="cbg0" renderRange="1-2" converter="salutationId">
            <f:selectItems value="#{overviewController.items}" id="itemsg0" />
          </tc:selectManyCheckbox>
          <tc:selectOneChoice value="#{overviewController.singleValue}"
              converter="salutationId" >
            <f:selectItems value="#{overviewController.items}" />
          </tc:selectOneChoice>
        </tc:panel>
      </tc:panel>

      <%-- row --%>
      <tc:separator>
        <f:facet name="label">
          <tc:label value="#{overviewBundle.basic_sampleLinkTitle}"/>
        </f:facet>
      </tc:separator>

      <%-- row --%>
      <tc:out escape="false" value="#{overviewBundle.basic_text_action}" />

      <%-- row --%>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="120px;*;120px;*;120px" rows="fixed;fixed" border="0"/>
        </f:facet>
        <tc:link id="link" action="overview/basic"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.basic_linkAction}" />
        <tc:cell/>
        <tc:link id="image" action="overview/basic"
            actionListener="#{overviewController.click}"
            image="image/image_button.gif" />
        <tc:cell/>
        <tc:button id="button" action="overview/basic"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.basic_buttonAction}" />

        <tc:cell spanX="5">
          <tx:in value="#{overviewController.lastAction}" readonly="true"
            label="#{overviewBundle.basic_lastActionLabel}" />
        </tc:cell>
      </tc:panel>

    </tc:box>
  </jsp:body>
</layout:overview>
