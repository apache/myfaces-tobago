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

  <tc:page label="Sandbox - Tree" id="page" width="500px" height="1000px">
    <f:facet name="layout">
      <tc:gridLayout margin="10px" rows="*;*;*"/>
    </f:facet>

    <tc:treeMenu id="menu" showRoot="true">
      <tc:treeNode label="Root" id="root" expanded="true">
        <tc:treeData value="#{treeController.tree}" var="node" id="data">
          <tc:treeNode label="#{node.userObject.name}"
                       id="template"
                       expanded="#{node.userObject.expanded}"
                       markup="#{node.userObject.markup}"
                       tip="#{node.userObject.tip}"
                       action="#{node.userObject.action}"
                       disabled="#{node.userObject.disabled}"/>
        </tc:treeData>
        <tc:treeNode label="2 Action 1" action="#{treeController.action1}" id="action1"/>
        <tc:treeNode label="3 Action 2" action="#{treeController.action2}" id="action2"/>
        <tc:treeNode label="4 Action 3" action="#{treeController.action3}" id="action3">
          <tc:treeNode label="4.1 On Click 1" onclick="alert('On Click 1');" id="click1"/>
          <tc:treeNode label="4.2 On Click 2" onclick="alert('On Click 2');" id="click2">
            <tc:treeNode label="4.2.1 On Click 3" onclick="alert('On Click 3');" id="click3"/>
          </tc:treeNode>
        </tc:treeNode>
        <tc:treeNode label="5 Link" link="http://myfaces.apache.org/tobago/" id="link" tip="Subnode Link"/>
        <tc:treeNode label="6 Target" action="#{treeController.action2}" target="Target Window"/>
      </tc:treeNode>
    </tc:treeMenu>

    <tc:treeMenu>
      <tc:treeData value="#{treeController.tree}" var="node" id="data">
        <tc:treeNode label="#{node.userObject.name}"
                     id="template"
                     expanded="#{node.userObject.expanded}"
                     markup="#{node.userObject.markup}"
                     tip="#{node.userObject.tip}"
                     action="#{node.userObject.action}"
                     disabled="#{node.userObject.disabled}"/>
      </tc:treeData>
    </tc:treeMenu>

    <tc:treeMenu>
      <tc:treeNode label="Root" expanded="true">
        <tc:treeNode label="2 Action 1"/>
        <tc:treeNode label="3 Action 2"/>
        <tc:treeNode label="4 Action 3">
          <tc:treeNode label="4.1 On Click 1" onclick="alert('On Click 1');"/>
          <tc:treeNode label="4.2 On Click 2" onclick="alert('On Click 2');">
            <tc:treeNode label="4.2.1 On Click 3" onclick="alert('On Click 3');"/>
          </tc:treeNode>
        </tc:treeNode>
        <tc:treeNode label="5 Link" link="http://myfaces.apache.org/tobago/" tip="Subnode Link"/>
        <tc:treeNode label="6 Target" target="Target Window"/>
      </tc:treeNode>
    </tc:treeMenu>

    <tc:cell/>

  </tc:page>
</f:view>
