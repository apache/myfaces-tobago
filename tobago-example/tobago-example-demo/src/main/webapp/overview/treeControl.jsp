<%--
 * Copyright 2002-2005 The Apache Software Foundation.
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
        <f:facet name="layout">
          <tc:gridLayout columns="2*;1*" />
        </f:facet>

        <tc:tabGroup state="#{overviewController.treeTabsState}" serverside="true">
          <tc:tab label="#{overviewBundle.treeLabel}">
            <tc:tree value="#{demo.tree}" state="#{demo.treeState}" id="tree"
                idReference="userObject.id"
                nameReference="userObject.name"
                showIcons="#{demo.showIcons}"
                showJunctions="#{demo.showJunctions}"
                showRootJunction="#{demo.showRootJunction}"
                showRoot="#{demo.showRoot}"
                selectable="#{overviewController.treeSelectMode}"
                mutable="#{demo.mutable}"
                >
              <f:actionListener type="org.apache.myfaces.tobago.example.demo.actionlistener.TreeEditor" />
            </tc:tree>
          </tc:tab>
          <tc:tab label="#{overviewBundle.treeListboxLabel}">

            <tc:treeListbox value="#{demo.tree}" state="#{demo.treeState}" id="treeListbox"
                idReference="userObject.id"
                nameReference="userObject.name"
                selectable="#{overviewController.treeListboxSelectMode}"
                >
            </tc:treeListbox>
          </tc:tab>
        </tc:tabGroup>

      <%-- column --%>
        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout rows="20px;fixed;fixed;fixed;fixed;1*;fixed;fixed;fixed;fixed" />
          </f:facet>

          <tc:cell />

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
          <tc:selectOneChoice value="#{overviewController.treeSelectMode}"
              rendered="#{overviewController.treeTabsState == 0}">
            <f:selectItems value="#{overviewController.treeSelectModeItems}" />
          </tc:selectOneChoice>
          <tc:selectOneChoice value="#{overviewController.treeListboxSelectMode}"
              rendered="#{overviewController.treeTabsState == 1}">
            <f:selectItems value="#{overviewController.treeListboxSelectModeItems}" />
          </tc:selectOneChoice>

          <tc:selectBooleanCheckbox label="#{overviewBundle.treeMutable}"
              value="#{demo.mutable}"
              disabled="#{overviewController.treeTabsState != 0}"/>
          <tc:button action="redisplay" label="#{overviewBundle.submit}" />
        </tc:panel>

      </tc:box>

    </tc:panel>
  </jsp:body>
</layout:overview>
