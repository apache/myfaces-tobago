<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="text_jsp" >

  <t:messages />

  <t:box label="#{bundle.objectBox}" height="500px" >
    <f:facet name="layout">
      <t:gridLayout columns="1*;100px" rows="fixed;fixed;10px;1*" />
    </f:facet>
    <t:cell spanX="2">
      <t:out value="#{bundle.objectDescription}" />
    </t:cell>

    <t:selectOneChoice value="#{demo.objectSrc}" label="#{bundle.objectSelectType}" >
      <f:facet name="layout" ><t:labeledInputLayout layout="1*;100px;0px" /></f:facet>
      <f:selectItem itemLabel="pdf" itemValue="resource/object.pdf" />
      <f:selectItem itemLabel="flash" itemValue="resource/object.swf" />
      <f:selectItem itemLabel="html" itemValue="resource/object.html" />
      <f:selectItem itemLabel="xml" itemValue="WEB-INF/web.xml" />
    </t:selectOneChoice>
    <t:button label="#{bundle.submit}" />
    
    <t:cell spanX="2"><hr align="middle" /></t:cell>       

    <t:cell spanX="2">
      <t:object src="#{demo.objectSrc}" />
    </t:cell>
  </t:box>


</f:subview>
