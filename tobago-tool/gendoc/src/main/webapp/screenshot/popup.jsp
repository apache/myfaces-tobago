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

<layout:screenshot>
  <jsp:body>
    <f:subview id="button">
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="100px;*" rows="fixed;*;*"/>
        </f:facet>

        <tc:button label="Open">
          <tc:attribute name="renderedPartially" value="popup"/>
          <f:facet name="popup">
            <tc:popup width="300" height="270" id="popup">
              <tc:box label="Text input">
                <f:facet name="layout">
                  <tc:gridLayout rows="fixed;fixed;fixed;*;fixed"/>
                </f:facet>

                <tc:selectBooleanCheckbox value="#{reference.bool}"/>
                <tc:in value="#{reference.text}" required="true"/>
                <tx:date>
                  <f:convertDateTime pattern="dd/MM/yyyy" />
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
                    <tc:attribute name="renderedPartially" value="popup"/>
                  </tc:button>
                  <tc:button label="Ok">
                    <tc:attribute name="popupClose" value="afterSubmit"/>
                  </tc:button>
                </tc:panel>

              </tc:box>
            </tc:popup>
          </f:facet>
        </tc:button>

        <tc:menuBar>
          <tc:menu label="Menu">
            <tc:menuItem label="Open Popup">
              <tc:popupReference for=":page:button:popup"/>
            </tc:menuItem>
          </tc:menu>
        </tc:menuBar>

        <tc:cell/>
        <tc:out value="here is the boolean >>#{reference.bool}<<"/>

        <tc:cell/>
        <tc:out value="here is the text >>#{reference.text}<<"/>

      </tc:panel>
    </f:subview>
  </jsp:body>
</layout:screenshot>