<%@ page import="org.apache.commons.logging.Log,
                 org.apache.commons.logging.LogFactory"%>
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%try{%>
<f:subview id="tree_jsp" >
  <t:box label="Tree" height="350px">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:treeListbox value="#{demo.tree}" state="#{demo.treeState}" id="tree"
        selectable="singleLeafOnly"
        idReference="userObject.id"
        nameReference="userObject.name"
        >
      <f:facet name="layout">
        <t:gridLayout columns="1*;1*" rows="1*;1*;1*" />
      </f:facet>
    </t:treeListbox>
        
  </t:box>
  <t:button action="redisplay">
<%--    <f:actionListener type="org.apache.myfaces.tobago.demo.actionlistener.TreeSubmit" />--%>
    <t:out value="#{bundle.submit}" />
  </t:button>
</f:subview>
<%
} catch (Throwable e) {
  Log log = LogFactory.getLog("org.apache.myfaces.tobago.demo.jsp");
  if (log.isErrorEnabled()) {
    log.error("", e);
  }
}
%>
