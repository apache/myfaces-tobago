<%--
 * Copyright 2002-2005 The Apache Software Foundation.
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
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>

<layout:overview>
  <jsp:body>
    <tc:panel >
      <f:facet name="layout">
        <tc:gridLayout border="0" rows="20px;fixed;1*;20px;fixed;1*;20px;fixed" />
  <%--      <tc:gridLayout border="0" rows="1*;6*;1*;1*;6*;1*;1*;6*" />--%>
      </f:facet>

  <%--row    --%>
      <tc:out escape="false" value="#{overviewBundle.basic_text_input}" />

  <%--row    --%>
      <tc:box label="#{overviewBundle.basicControls_sampleInputTitle}" >
        <f:facet name="layout">
          <tc:gridLayout columns="1*;1*"  border="0" />
        </f:facet>

        <tc:panel >
          <f:facet name="layout">
            <tc:gridLayout />
          </f:facet>
          <tx:label value="#{overviewBundle.basic_textboxLabel}" tip="test">
            <tc:in value="#{overviewController.basicInput}"
              suggestMethod="#{overviewController.getInputSuggestItems}"/>
          </tx:label>
          <tx:date value="#{overviewController.basicDate}"
              label="#{overviewBundle.basic_dateLabel}">
            <f:convertDateTime pattern="dd.MM.yyyy" />
          </tx:date>
          <tx:time label="#{overviewBundle.basic_timeLabel}"
                  value="#{overviewController.basicTime}" />
        </tc:panel>

        <tx:textarea value="#{overviewController.basicArea}"
          label="#{overviewBundle.basic_textareaLabel}" />
      </tc:box>

  <%--row    --%>
      <tc:cell /> <%-- spacer--%>

  <%--row    --%>
      <tc:out escape="false" value="#{overviewBundle.basic_text_select}" />

  <%--row    --%>
      <tc:box label="#{overviewBundle.basicControls_sampleSelectTitle}" >
        <f:facet name="layout">
          <tc:gridLayout columns="1*;1*"  border="0"/>
        </f:facet>
        <tc:selectOneRadio value="#{overviewController.radioValue}" id="rg0">
          <f:selectItems value="#{overviewController.items}" id="items0" />
        </tc:selectOneRadio>

        <tc:panel >
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed"/>
          </f:facet>
          <tc:selectManyCheckbox value="#{overviewController.multiValue}" id="cbg0" renderRange="1-2" >
            <f:selectItems value="#{overviewController.items}" id="itemsg0" />
          </tc:selectManyCheckbox>
          <tc:selectOneChoice value="#{overviewController.singleValue}">
            <f:selectItems value="#{overviewController.items}" />
          </tc:selectOneChoice>
        </tc:panel>
      </tc:box>
  <%--row    --%>
      <tc:cell /> <%-- spacer--%>

  <%--row    --%>
      <tc:out escape="false" value="#{overviewBundle.basic_text_action}" />

  <%--row    --%>
      <tc:box label="#{overviewBundle.basicControls_sampleLinkTitle}" >
        <f:facet name="layout">
          <tc:gridLayout columns="1*;1*;1*" border="0"/>
        </f:facet>
        <tc:link id="link" action="overview/basicControls"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.basic_linkAction}" />
        <tc:link id="image" action="overview/basicControls"
            actionListener="#{overviewController.click}"
            image="image/image_button.gif" />
        <tc:button id="button" action="overview/basicControls"
            actionListener="#{overviewController.click}"
            width="100px"  label="#{overviewBundle.basic_buttonAction}" />
        <tc:cell spanX="3">
          <tx:in value="#{overviewController.lastAction}" readonly="true"
            label="#{overviewBundle.basic_lastActionLabel}" />
        </tc:cell>
      </tc:box>
    </tc:panel>
  </jsp:body>
</layout:overview>
