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

<%@ page pageEncoding="UTF-8" %>

<f:view>
  <tc:page width="500" height="300" id="page">

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout margin="10px" rows="fixed;*;fixed"/>
      </f:facet>

      <tc:box label="Test" id="panel">
        <f:facet name="layout">
          <tc:gridLayout rows="fixed" columns="*;2*"/>
        </f:facet>
        <tc:form>
          <tx:selectBooleanCheckbox label="Don't process" value="#{controller.suppressProcessing}">
            <f:facet name="change">
              <tc:command>
                <%--
                <tc:attribute name="renderedPartially" value=":page:panel"/>
                --%>
              </tc:command>
            </f:facet>
          </tx:selectBooleanCheckbox>
        </tc:form>
        <tx:in label="Input" required="#{not controller.suppressProcessing}"/>
      </tc:box>

      <tc:sheet value="#{controller.suppressProcessingList}" var="row" columns="*;2*">
        <tc:column label="Don't process">
          <tc:form>
            <tc:selectBooleanCheckbox value="#{row.suppressProcessing}">
              <f:facet name="change">
                <tc:command>
                  <%--
                  <tc:attribute name="renderedPartially" value=":page:panel"/>
                  --%>
                </tc:command>
              </f:facet>
            </tc:selectBooleanCheckbox>
          </tc:form>
        </tc:column>
        <tc:column label="Input">
          <tc:in value="#{row.input}" required="#{not row.suppressProcessing}"/>
        </tc:column>
      </tc:sheet>
      <tc:button label="Submit"/>
    </tc:panel>
  </tc:page>
</f:view>
