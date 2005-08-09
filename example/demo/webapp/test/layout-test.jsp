<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: layout-test.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="test of panel" id="page">
      <center>
      <t:box label="Layout test" width="304px" height="250px">
        <f:facet name="layout">
          <t:gridLayout border="5" rows="fixed;*;fixed;fixed" />
        </f:facet>

        <t:in value="#{demo.text[1]}" label="Textbox" />

        <t:textarea label="Textarea" value="#{demo.text[5]}" />

        <t:selectManyListbox value="#{demo.phoneProtocols[0]}" label="Multiselect">
          <f:selectItems value="#{demo.phoneProtocols[0]}" />
        </t:selectManyListbox>

        <t:selectOneChoice value="#{demo.salutation[0]}" label="Singleselect">
          <f:selectItems value="#{demo.salutation[0]}" />
        </t:selectOneChoice>

      </t:box>

      <t:box label="Empty Layout" width="304px" height="50px">
        <f:facet name="layout">
          <t:gridLayout border="5" rows="fixed;*;fixed;fixed" />
        </f:facet>

      </t:box>

      </center>
  </t:page>
</f:view>
