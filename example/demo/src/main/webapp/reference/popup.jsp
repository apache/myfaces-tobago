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
<tc:box label="Popups">
<f:facet name="layout">
  <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;*"/>
</f:facet>

<tc:menuBar>
  <tc:menu label="Menu">
    <tc:menuItem label="Open Modal Popup">
      <tc:popupReference for="modal_popup"/>
    </tc:menuItem>
    <tc:menuItem label="Open Non Modal Popup">
      <tc:popupReference for="non_modal_popup"/>
    </tc:menuItem>
    <tc:menuItem label="Open Modal Popup (Partially)">
      <tc:attribute name="renderedPartially" value="modal_popup"/>
      <tc:popupReference for="modal_popup"/>
    </tc:menuItem>
    <tc:menuItem label="Open Non Modal Popup (Partially)">
      <tc:attribute name="renderedPartially" value="non_modal_popup"/>
      <tc:popupReference for="non_modal_popup"/>
    </tc:menuItem>
  </tc:menu>
</tc:menuBar>

<tc:messages/>

<tc:panel>
  <f:facet name="layout">
    <tc:gridLayout columns="fixed;fixed;fixed;fixed;*"/>
  </f:facet>

  <tc:label value="Full Reload"/>

  <tc:button label="Open">
    <f:facet name="popup">
      <tc:popup width="300" height="270" id="modal_popup">
        <tc:box label="Modal text input" id="modal_box">
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed;fixed;*;fixed"/>
          </f:facet>

          <tc:selectBooleanCheckbox value="#{reference.bool}"/>
          <tc:in value="#{reference.text}" required="true"/>
          <tx:date>
            <f:convertDateTime pattern="dd/MM/yyyy"/>
          </tx:date>
          <tc:cell/>
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout columns="1*;1*;1*" margin="10"/>
            </f:facet>
            <tc:button label="Cancel">
              <tc:attribute name="popupClose" value="immediate"/>
            </tc:button>
            <tc:button label="Redisplay">
              <tc:attribute name="renderedPartially" value="modal_box"/>
            </tc:button>
            <tc:button label="Ok">
              <tc:attribute name="popupClose" value="afterSubmit"/>
            </tc:button>
          </tc:panel>

        </tc:box>
      </tc:popup>
    </f:facet>
  </tc:button>

  <tc:button label="Open modeless">
    <f:facet name="popup">
      <tc:popup width="300" height="270" id="non_modal_popup" modal="false">
        <tc:box label="Non modal text input">
          <f:facet name="layout">
            <tc:gridLayout rows="fixed;fixed;*;fixed"/>
          </f:facet>

          <tx:in label="Field" required="true"/>
          <tx:date>
            <f:convertDateTime pattern="dd/MM/yyyy"/>
          </tx:date>
          <tc:cell/>
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout columns="*;fixed;fixed"/>
            </f:facet>
            <tc:cell/>
            <tc:button label="Cancel">
              <tc:attribute name="popupClose" value="immediate"/>
            </tc:button>
            <tc:button label="Ok">
              <tc:attribute name="popupClose" value="afterSubmit"/>
            </tc:button>
          </tc:panel>

        </tc:box>
      </tc:popup>
    </f:facet>
  </tc:button>

  <tc:button label="Open here">
    <f:facet name="popup">
      <tc:popup width="180" height="110" id="positioned_popup"
                left="#{tobago.actionPosition.right.pixel + 5}" top="#{tobago.actionPosition.top.pixel}">
        <tc:box label="Info">
          <f:facet name="layout">
            <tc:gridLayout rows="*;fixed"/>
          </f:facet>

          <tc:textarea value="This popup should opened right beside the button." readonly="true"/>
          <tc:button label="Ok">
            <tc:attribute name="popupClose" value="immediate"/>
          </tc:button>

        </tc:box>
      </tc:popup>
    </f:facet>
  </tc:button>

  <tc:cell/>

</tc:panel>
<tc:panel>
  <f:facet name="layout">
    <tc:gridLayout columns="fixed;fixed;fixed;fixed;*"/>
  </f:facet>

  <tc:label value="Partial Reload"/>

  <tc:button label="Open">
    <tc:attribute name="renderedPartially" value="modal_popup"/>
    <tc:popupReference for="modal_popup"/>
  </tc:button>

  <tc:button label="Open modeless">
    <tc:attribute name="renderedPartially" value="non_modal_popup"/>
    <tc:popupReference for="non_modal_popup"/>
  </tc:button>

  <tc:button label="Open here">
    <tc:attribute name="renderedPartially" value="positioned_popup"/>
    <tc:popupReference for="positioned_popup"/>
  </tc:button>

  <tc:cell/>

</tc:panel>

<tx:in readonly="true" label="The Boolean" value="#{reference.bool}"/>

<tx:in readonly="true" label="The Text" value="#{reference.text}"/>

<tx:in required="true" label="Required"/>

<tx:selectOneChoice label="Select"/>

<tc:cell/>

</tc:box>
</jsp:body>
</layout:overview>
