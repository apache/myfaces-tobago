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


<f:view>
  <tc:page id="inputPage" width="300px" height="200px">
    <tc:panel id="inputPanel">
      <tc:button id="popupButton"
        label="Popup">
        <f:facet name="popup">
          <tc:popup id="printPopup" width="400" height="125">

            <tc:box id="box"
              label="Popup">
              <f:facet name="layout">
                <tc:gridLayout
                  rows="40px;40px;1*;auto"
                  columns="*"/>
              </f:facet>

              <tc:out id="text1" rendered="false"
                value="Text 1"/>
              <tc:out id="text2" rendered="true"
                value="Text 2"/>

              <tc:panel id="cell"/>

              <tc:panel id="buttonPanel">
                <f:facet name="layout">
                  <tc:gridLayout rows="auto"
                    columns="*;*;*"/>
                </f:facet>
                <tc:button id="button1"
                  label="button1">
                  <tc:attribute name="popupClose" value="afterSubmit"/>
                </tc:button>
                <tc:button id="button2"
                  label="button2">
                  <tc:attribute name="popupClose" value="afterSubmit"/>
                </tc:button>
                <tc:button id="button3"
                  label="button3">
                  <tc:attribute name="popupClose" value="immediate"/>
                </tc:button>
              </tc:panel>
            </tc:box>
          </tc:popup>
        </f:facet>
      </tc:button>
    </tc:panel>
  </tc:page>
</f:view>
