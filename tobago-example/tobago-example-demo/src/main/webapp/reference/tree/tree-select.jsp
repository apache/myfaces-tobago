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

  <tc:page label="Sandbox - Tree" id="page"
           width="500px" height="800px">
    <f:facet name="layout">
      <tc:gridLayout margin="10px" rows="300px;*"/>
    </f:facet>

    <tc:tree id="sel"
              showIcons="true"
              showRootJunction="true"
              showRoot="true"
              selectable="single">
      <tc:treeNode label="Root">
        <tc:treeData value="#{treeController.tree}" var="node">
          <tc:treeNode label="#{node.userObject.name}"
                        markup="#{node.userObject.markup}"
                        tip="#{node.userObject.tip}"/>
        </tc:treeData>
        <tc:treeNode label="Sub 1"/>
        <tc:treeNode label="Sub 2"/>
        <tc:treeNode label="Sub 3">
          <tc:treeNode label="Sub 3.1"/>
          <tc:treeNode label="Sub 3.2"/>
        </tc:treeNode>
        <tc:treeNode label="Sub 4" tip="Subnode Number 4"/>
      </tc:treeNode>
    </tc:tree>

    <tc:cell/>

  </tc:page>
</f:view>
