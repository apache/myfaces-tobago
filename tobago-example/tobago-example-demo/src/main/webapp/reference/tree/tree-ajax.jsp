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

<f:view>
  <tc:loadBundle basename="demo" var="bundle"/>

  <tc:page label="Sandbox - Tree with AJAX" id="page"
           width="500px" height="800px">
    <f:facet name="layout">
      <tc:gridLayout margin="10px" rows="fixed;600px;*"/>
    </f:facet>

    <tc:out value="This only demonstrate the standard reload, not individual node reloading which is not implemented yet." />

    <tc:panel>
      <f:facet name="reload">
        <tc:reload frequency="2000"/>
      </f:facet>

      <tc:tree state="#{controller.state}" id="tree"
                showIcons="true"
                showRootJunction="true"
                showRoot="true"
          >
        <tc:treeNode label="Root" id="root" expanded="true">
          <tc:treeData value="#{controller.tree}" var="node" id="data">
            <tc:treeNode label="#{node.userObject.name}"
                          id="template"
                          expanded="#{node.userObject.expanded}"
                          markup="#{node.userObject.markup}"
                          tip="#{node.userObject.tip}"
                          action="#{node.userObject.action}"
                          disabled="#{node.userObject.disabled}"/>
          </tc:treeData>
          <tc:treeNode label="2 Action 1" action="#{controller.action1}" id="action1"/>
          <tc:treeNode label="3 Action 2" action="#{controller.action2}" id="action2"/>
          <tc:treeNode label="4 Action 3" action="#{controller.action3}" id="action3">
            <tc:treeNode label="4.1 On Click 1" onclick="alert('On Click 1');" id="click1"/>
            <tc:treeNode label="4.2 On Click 2" onclick="alert('On Click 2');" id="click2">
              <tc:treeNode label="4.2.1 On Click 3" onclick="alert('On Click 3');" id="click3"/>
            </tc:treeNode>
          </tc:treeNode>
          <tc:treeNode label="5 Link" link="http://myfaces.apache.org/tobago/" id="link" tip="Subnode Link"/>
          <tc:treeNode label="6 Target" action="#{controller.action2}" target="Target Window"/>
          <tc:treeNode label="7 Time: #{controller.currentTime}" />
        </tc:treeNode>
      </tc:tree>
    </tc:panel>
    <tc:cell/>

  </tc:page>
</f:view>
