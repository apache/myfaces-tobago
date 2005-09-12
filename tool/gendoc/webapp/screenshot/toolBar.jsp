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

<layout:screenshot>
  <f:subview id="toolBar">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="60px;1*" columns="260px;1*"/>
        </f:facet>

            <t:toolBar id="toolbar0" iconSize="big">
              <t:toolBarCommand id="button0" label="Button" />
              <t:toolBarCommand id="button1" label="Accesskey Button" accessKey="B"/>
              <t:toolBarCommand id="button2" label="Image Button"
                                image="image/toolbar_example_button.gif" />
              <t:toolBarCommand id="button3" disabled="true" label="Disabled Button" />
            </t:toolBar>

        <t:cell/>
        <t:cell/>
        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>

