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

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="9*;16*" />
        <%-- <t:gridLayout rows="8*;17*" />--%>
      </f:facet>

        <t:out escape="false" value="#{overviewBundle.tree_text}" />

      <%-- column --%>

      <t:box label="#{overviewBundle.tree_sampleTitle}" >
        <f:facet name="layout">
          <t:gridLayout columns="2*;1*" />
        </f:facet>

        <t:tabGroup state="#{overviewController.treeTabsState}" serverside="true">
          <t:tab label="#{overviewBundle.treeLabel}">
            <t:tree value="#{demo.tree}" state="#{demo.treeState}" id="tree"
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
            </t:tree>
          </t:tab>
          <t:tab label="#{overviewBundle.treeListboxLabel}">

            <t:treeListbox value="#{demo.tree}" state="#{demo.treeState}" id="treeListbox"
                idReference="userObject.id"
                nameReference="userObject.name"
                selectable="#{overviewController.treeListboxSelectMode}"
                >
            </t:treeListbox>
          </t:tab>
        </t:tabGroup>

      <%-- column --%>
        <t:panel>
          <f:facet name="layout">
            <t:gridLayout rows="20px;fixed;fixed;fixed;fixed;1*;fixed;fixed;fixed;fixed" />
          </f:facet>

          <t:cell />

          <t:selectBooleanCheckbox label="#{overviewBundle.treeShowIcons}"
              value="#{demo.showIcons}"
              disabled="#{overviewController.treeTabsState != 0}"/>
          <t:selectBooleanCheckbox label="#{overviewBundle.treeShowJunctions}"
              value="#{demo.showJunctions}"
              disabled="#{overviewController.treeTabsState != 0}"/>
          <t:selectBooleanCheckbox label="#{overviewBundle.treeShowRootJunction}"
              value="#{demo.showRootJunction}"
              disabled="#{overviewController.treeTabsState != 0}"/>
          <t:selectBooleanCheckbox label="#{overviewBundle.treeShowRoot}"
              value="#{demo.showRoot}"
              disabled="#{overviewController.treeTabsState != 0}"/>
          <t:cell />
          <t:selectOneChoice value="#{overviewController.treeSelectMode}"
              rendered="#{overviewController.treeTabsState == 0}">
            <f:selectItems value="#{overviewController.treeSelectModeItems}" />
          </t:selectOneChoice>
          <t:selectOneChoice value="#{overviewController.treeListboxSelectMode}"
              rendered="#{overviewController.treeTabsState == 1}">
            <f:selectItems value="#{overviewController.treeListboxSelectModeItems}" />
          </t:selectOneChoice>

          <t:selectBooleanCheckbox label="#{overviewBundle.treeMutable}"
              value="#{demo.mutable}"
              disabled="#{overviewController.treeTabsState != 0}"/>
          <t:button action="redisplay" label="#{overviewBundle.submit}" />
        </t:panel>

      </t:box>

    </t:panel>
  </jsp:body>
</layout:overview>
