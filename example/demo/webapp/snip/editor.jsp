<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: editor.jsp 1178 2005-02-22 11:29:05 +0100 (Di, 22 Feb 2005) lofwyr $
  --%><%@
 page errorPage="/errorPage.jsp" %><%@
 taglib uri="http://www.atanion.com/tobago/component" prefix="t" %><%@
 taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="editor_jsp" >

  <t:messages />

  <t:box label="#{bundle.richTextEditor}" height="300px" >
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:richTextEditor value="#{demo.text[10]}" />

  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button label="#{bundle.submit}" />

    <t:cell />

  </t:panel>
</f:subview>
