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

<layout:overview>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="9*;16*" />
        <%-- <tc:gridLayout rows="8*;17*" />--%>
      </f:facet>

        <tc:out escape="false" value="#{overviewBundle.tree_text}" />

      <%-- column --%>

      <tc:box label="#{overviewBundle.tree_sampleTitle}" >
        <tc:tabGroup selectedIndex="#{overviewController.treeTabsState}" switchType="reloadPage">
          <tc:tab label="#{overviewBundle.treeLabel}">
        <f:facet name="layout">
          <tc:gridLayout columns="2*;1*" />
        </f:facet>

            <tc:tree value="#{demo.tree}" state="#{demo.treeState}" id="tree"
                idReference="userObject.id"
                nameReference="userObject.name"
                showIcons="#{demo.showIcons}"
                showJunctions="#{demo.showJunctions}"
                showRootJunction="#{demo.showRootJunction}"
                showRoot="#{demo.showRoot}"
                selectable="#{overviewController.treeSelectMode}"
                mutable="#{demo.mutable}"
                tipReference="userObject.name"                
                >
              <f:actionListener type="org.apache.myfaces.tobago.example.demo.actionlistener.TreeEditor" />
            </tc:tree>

            <tc:panel>
              <f:facet name="layout">
                <tc:gridLayout rows="fixed;fixed;fixed;fixed;1*;fixed;fixed;fixed" />
              </f:facet>

              <tc:selectBooleanCheckbox label="#{overviewBundle.treeShowIcons}"
                                        value="#{demo.showIcons}"
                                        disabled="#{overviewController.treeTabsState != 0}"/>
              <tc:selectBooleanCheckbox label="#{overviewBundle.treeShowJunctions}"
                                        value="#{demo.showJunctions}"
                                        disabled="#{overviewController.treeTabsState != 0}"/>
              <tc:selectBooleanCheckbox label="#{overviewBundle.treeShowRootJunction}"
                                        value="#{demo.showRootJunction}"
                                        disabled="#{overviewController.treeTabsState != 0}"/>
              <tc:selectBooleanCheckbox label="#{overviewBundle.treeShowRoot}"
                                        value="#{demo.showRoot}"
                                        disabled="#{overviewController.treeTabsState != 0}"/>
              <tc:cell />
              <tc:selectOneChoice value="#{overviewController.treeSelectMode}">
                <f:selectItems value="#{overviewController.treeSelectModeItems}" />
              </tc:selectOneChoice>

              <tc:selectBooleanCheckbox label="#{overviewBundle.treeMutable}"
                                        value="#{demo.mutable}"
                                        disabled="#{overviewController.treeTabsState != 0}"/>
              <tc:button action="redisplay" label="#{overviewBundle.submit}" />
            </tc:panel>

          </tc:tab>
          <tc:tab label="#{overviewBundle.treeListboxLabel}">

            <f:facet name="layout">
              <tc:gridLayout columns="2*;1*" />
            </f:facet>

            <tc:treeListbox value="#{demo.tree}" state="#{demo.treeState}" id="treeListbox"
                idReference="userObject.id"
                nameReference="userObject.name"
                selectable="#{overviewController.treeListboxSelectMode}"
                tipReference="userObject.name"
                >
            </tc:treeListbox>

            <tc:panel>
              <f:facet name="layout">
                <tc:gridLayout rows="1*;fixed;20px;fixed" />
              </f:facet>

              <tc:cell />

              <tc:selectOneChoice value="#{overviewController.treeListboxSelectMode}">
                <f:selectItems value="#{overviewController.treeListboxSelectModeItems}" />
              </tc:selectOneChoice>

              <tc:cell />

              <tc:button action="redisplay" label="#{overviewBundle.submit}" />
            </tc:panel>
          </tc:tab>
        </tc:tabGroup>

      </tc:box>

    </tc:panel>
  </jsp:body>
</layout:overview>
