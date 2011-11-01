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
    <tc:box label="Tool Bar">
      <f:facet name="layout">
        <tc:gridLayout rows="auto;*"/>
      </f:facet>

      <tc:toolBar id="toolbar0" iconSize="big">
        <tc:toolBarCommand id="button0" label="Button"/>
        <tc:toolBarCommand id="button1" label="Accesskey _Button"/>
        <tc:toolBarCommand id="button2" label="Image Button"
                           image="image/toolbar_example_button.gif"/>
        <tc:toolBarCommand id="button3" disabled="true" label="Disabled Button"/>
      </tc:toolBar>

      <tc:cell/>
    </tc:box>
  </jsp:body>
</layout:overview>
