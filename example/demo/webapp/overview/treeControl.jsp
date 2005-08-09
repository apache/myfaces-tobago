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
          <t:gridLayout rows="fixed;fixed;fixed;fixed;1*;fixed;fixed;fixed"
            columns="2*;1*" />
        </f:facet>

      <t:cell spanY="8">
        <t:tree value="#{demo.tree}" state="#{demo.treeState}" id="tree"
            idReference="userObject.id"
            nameReference="userObject.name"
            showIcons="#{demo.showIcons}"
            showJunctions="#{demo.showJunctions}"
            showRootJunction="#{demo.showRootJunction}"
            showRoot="#{demo.showRoot}"
            selectable="#{demo.selectionType}"
            mutable="#{demo.mutable}"
            >
          <f:actionListener type="com.atanion.tobago.demo.actionlistener.TreeEditor" />
        </t:tree>
      </t:cell>

      <%-- column --%>

      <t:selectBooleanCheckbox label="#{bundle.treeShowIcons}"
          value="#{demo.showIcons}" />
      <t:selectBooleanCheckbox label="#{bundle.treeShowJunctions}"
          value="#{demo.showJunctions}" />
      <t:selectBooleanCheckbox label="#{bundle.treeShowRootJunction}"
          value="#{demo.showRootJunction}" />
      <t:selectBooleanCheckbox label="#{bundle.treeShowRoot}"
          value="#{demo.showRoot}" />
      <t:cell />
      <t:selectOneChoice value="#{demo.selectionType}">
        <f:selectItems value="#{demo.selectionItems}" />
      </t:selectOneChoice>

      <t:selectBooleanCheckbox label="#{bundle.treeMutable}"
          value="#{demo.mutable}" />
      <t:button action="redisplay" label="#{bundle.submit}" />

      </t:box>

    </t:panel>
  </jsp:body>
</layout:overview>
