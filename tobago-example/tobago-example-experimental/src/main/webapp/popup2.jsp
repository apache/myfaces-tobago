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
  <tc:page id="inputPage" width="500px" height="300px">
    <tc:panel id="inputPanel">
      <tc:button id="popupButton"
                 label="Popup">
        <f:facet name="popup">
          <tc:popup id="popup" width="400" height="220">

            <tc:box id="box" label="Popup">
              <f:facet name="layout">
                <tc:gridLayout rows="*;auto;auto;auto;54px;auto" columns="*;*;*"/>
              </f:facet>
              <tc:panel>
                <tc:gridLayoutConstraint columnSpan="3"/>
                <tc:out value="Text"/>
              </tc:panel>

              <tc:panel>
                <tc:gridLayoutConstraint columnSpan="2"/>
                <tc:label value="label1"/>
              </tc:panel>
              <tc:panel/>

              <tc:panel>
                <tc:gridLayoutConstraint columnSpan="2"/>
                <tc:selectOneChoice id="choice">
                  <tc:selectItem itemLabel="" itemValue=""/>
                </tc:selectOneChoice>
              </tc:panel>
              <tc:panel/>

              <tc:panel>
                <tc:gridLayoutConstraint columnSpan="3"/>
                <tc:label value="label2"/>
              </tc:panel>
              <tc:panel>
                <tc:gridLayoutConstraint columnSpan="3"/>
                <tc:textarea id="textarea"/>
              </tc:panel>

              <tc:panel/>
              <tc:button id="saveButton"
                         label="save">
                <tc:attribute name="popupClose" value="afterSubmit"/>
              </tc:button>
              <tc:button id="cancelButton"
                         label="cancel">
                <tc:attribute name="popupClose" value="immediate"/>
              </tc:button>
            </tc:box>
          </tc:popup>
        </f:facet>
      </tc:button>
    </tc:panel>
  </tc:page>
</f:view>  
