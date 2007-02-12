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

<%@ taglib uri="http://myfaces.apache.org/tobago/sandbox" prefix="tcs" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <tc:loadBundle basename="demo" var="bundle"/>

  <tc:page label="Sandbox - Tree" id="page"
           width="500px" height="800px">
    <f:facet name="layout">
      <tc:gridLayout margin="10px" rows="300px;*"/>
    </f:facet>

    <tcs:tree state="#{controller.state}" id="menu"
              showIcons="false"
              showJunctions="false"
              showRootJunction="false"
              showRoot="true"
              mode="menu">
      <tcs:treeNode label="Root" id="root">
        <tcs:treeNodes value="#{controller.tree}" var="node" id="data">
          <tcs:treeNode label="#{node.userObject.name}" id="template"
                        markup="#{node.userObject.markup}"
                        action="#{node.userObject.action}" value="#{node}"/>
        </tcs:treeNodes>
        <tcs:treeNode label="Action 1" action="#{controller.action1}" id="action1"/>
        <tcs:treeNode label="Action 2" action="#{controller.action2}" id="action2"/>
        <tcs:treeNode label="Action 3" action="#{controller.action3}" id="action3">
          <tcs:treeNode label="On Click 1" onclick="alert('On Click 1');" id="click1"/>
          <tcs:treeNode label="On Click 2" onclick="alert('On Click 2');" id="click2">
            <tcs:treeNode label="On Click 3" onclick="alert('On Click 3');" id="click3"/>
          </tcs:treeNode>
        </tcs:treeNode>
        <tcs:treeNode label="Link" link="http://myfaces.apache.org/tobago/" id="link"/>
      </tcs:treeNode>
    </tcs:tree>

    <tc:cell/>

  </tc:page>
</f:view>
