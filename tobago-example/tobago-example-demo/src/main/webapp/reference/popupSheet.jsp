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
    <tc:box label="Popups inside sheet">
      <f:facet name="layout">
        <tc:gridLayout />
      </f:facet>

      <tc:sheet id="sheet"
                var="entry" columns="1*;1*;1*;1*" value="#{popupReference.sheet}">
        <f:facet name="popup1" >
          <tc:popup id="popup1" width="300px" height="100px">
            <%-- the clientId of this popup is "page:sheet:popup1" --%>
            <tc:box label="Popup1">
              <f:facet name="layout" >
                <tc:gridLayout rows="auto;1*;auto"/>
              </f:facet>
              <tx:in label="column 1 value:" value="#{popupReference.entry.column1}"/>
              <tc:panel/>
              <tc:panel>
                <f:facet name="layout">
                  <tc:gridLayout columns="1*;auto;auto"/>
                </f:facet>
                <tc:panel/>
                <tc:button label="Save" actionListener="#{popupReference.saveChanges}" >
                  <tc:attribute name="renderedPartially" value=":page:sheet"/>
                  <tc:attribute name="popupClose" value="afterSubmit"/>
                </tc:button>
                <tc:button label="Cancel" >
                  <tc:attribute name="popupClose" value="immediate"/>
                </tc:button>
              </tc:panel>
            </tc:box>
          </tc:popup>
        </f:facet>
        <f:facet name="popup2" >
          <tc:popup id="popup2" width="300px" height="150px">
            <%-- the clientId of this popup is "page:sheet:popup2" --%>
            <tc:box label="Popup1">
              <f:facet name="layout" >
                <tc:gridLayout rows="auto;auto;auto;1*;auto"/>
              </f:facet>
              <tx:in label="column 1 value:" value="#{popupReference.entry.column1}"/>
              <tx:in label="column 2 value:" value="#{popupReference.entry.column2}"/>
              <tx:in label="column 3 value:" value="#{popupReference.entry.column3}"/>
              <tc:panel/>
              <tc:panel>
                <f:facet name="layout">
                  <tc:gridLayout columns="1*;auto;auto"/>
                </f:facet>
                <tc:panel/>
                <tc:button label="Save" actionListener="#{popupReference.saveChanges}" >
                  <tc:attribute name="renderedPartially" value=":page:sheet"/>
                  <tc:attribute name="popupClose" value="afterSubmit"/>
                </tc:button>
                <tc:button label="Cancel" >
                  <tc:attribute name="popupClose" value="immediate"/>
                </tc:button>
              </tc:panel>
            </tc:box>
          </tc:popup>
        </f:facet>

        <tc:column label="Column 1">
          <tc:link label="#{entry.column1}"
                   actionListener="#{popupReference.selectEntry}">
            <tc:popupReference for=":page:sheet:popup1"/>
            <tc:attribute name="renderedPartially" value=":page:sheet:popup1"/>
          </tc:link>
        </tc:column>

        <tc:column label="Column 2">
          <tc:out value="#{entry.column2}"/>
        </tc:column>

        <tc:column label="Column 3">
          <tc:out value="#{entry.column3}"/>
        </tc:column>

        <tc:column label="Edit">
          <tc:button label="Edit"
                     actionListener="#{popupReference.selectEntry}">
            <tc:popupReference for=":page:sheet:popup2"/>
            <tc:attribute name="renderedPartially" value=":page:sheet:popup2"/>
          </tc:button>
        </tc:column>


      </tc:sheet>

    </tc:box>
  </jsp:body>
</layout:overview>
