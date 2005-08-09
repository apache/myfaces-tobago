<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: multiform.jsp 1178 2005-02-22 11:29:05 +0100 (Di, 22 Feb 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page id="page">

    <t:form id="form1" >
      <t:in value="#{demo.text[0]}" required="true" />

      <t:button label="submit form 1" />
    </t:form>

    <t:out value="#{demo.text[0]}" />

    <br />

    <t:form id="form2">
      <t:in value="#{demo.text[1]}" required="true" />

      <t:button label="submit form 2" />
    </t:form>

    <t:out value="#{demo.text[1]}" />

    <br />

    <t:button label="submit whole page" />

  </t:page>
</f:view>
