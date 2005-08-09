<%@ page import="org.apache.commons.logging.Log,
                 org.apache.commons.logging.LogFactory"%>
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%try{%>
<f:subview id="tree_jsp" >
  <t:box label="Tree" height="350px">
    <f:facet name="layout">
      <t:gridLayout columns="1*;1*" rows="1*;fixed;fixed;fixed;fixed"  />
    </f:facet>

    <t:cell spanX="2">
      <t:tree state="#{demo.treeState}" value="#{demo.tree}" id="tree"
          idReference="userObject.id"
          nameReference="userObject.name"
          showIcons="#{demo.showIcons}"
          showJunctions="#{demo.showJunctions}"
          showRootJunction="#{demo.showRootJunction}"
          showRoot="#{demo.showRoot}"
          selectable="#{demo.selectionType}"
          mutable="#{demo.mutable}"
          required="#{demo.required}"
          >
        <f:actionListener type="com.atanion.tobago.demo.actionlistener.TreeEditor" />
      </t:tree>
    </t:cell>
    <%-- new row --%>
    <t:selectBooleanCheckbox label="#{bundle.treeShowIcons}"
        value="#{demo.showIcons}" />
    <t:selectOneChoice value="#{demo.selectionType}">
      <f:selectItems value="#{demo.selectionItems}" />
    </t:selectOneChoice>
    <%-- new row --%>
    <t:selectBooleanCheckbox label="#{bundle.treeShowJunctions}"
        value="#{demo.showJunctions}" />
    <t:selectBooleanCheckbox label="#{bundle.treeMutable}"
        value="#{demo.mutable}" />
    <%-- new row --%>
    <t:selectBooleanCheckbox label="#{bundle.treeShowRootJunction}"
        value="#{demo.showRootJunction}" />
    <t:selectBooleanCheckbox label="#{bundle.treeRequired}"
        value="#{demo.required}" />
    <%-- new row --%>
    <t:selectBooleanCheckbox label="#{bundle.treeShowRoot}"
        value="#{demo.showRoot}" />
    <f:verbatim />
  </t:box>
  <t:button action="redisplay">
<%--    <f:actionListener type="com.atanion.tobago.demo.actionlistener.TreeSubmit" />--%>
    <t:out value="#{bundle.submit}" />
  </t:button>
</f:subview>
<%
} catch (Throwable e) {
  Log log = LogFactory.getLog("com.atanion.tobago.demo.jsp");
  if (log.isErrorEnabled()) {
    log.error("", e);
  }
}
%>
