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

    <tcs:tree state="#{controller.state}" id="sel"
              showIcons="true"
              showJunctions="true"
              showRootJunction="true"
              showRoot="true"
              selectable="single">
      <tcs:treeNode label="Root">
        <tcs:treeNodeData value="#{controller.tree}" var="node">
          <tcs:treeNode label="#{node.userObject.name}"
                        markup="#{node.userObject.markup}"
              />
        </tcs:treeNodeData>
        <tcs:treeNode label="Sub 1"/>
        <tcs:treeNode label="Sub 2"/>
        <tcs:treeNode label="Sub 3">
          <tcs:treeNode label="Sub 3.1"/>
          <tcs:treeNode label="Sub 3.2"/>
        </tcs:treeNode>
        <tcs:treeNode label="Sub 4"/>
      </tcs:treeNode>
    </tcs:tree>

    <tc:cell/>

  </tc:page>
</f:view>
