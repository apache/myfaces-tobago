<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: multiselect.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <t:page label="multiselect test" id="page">

    <t:selectManyListbox height="85" value="#{demo.many}" id="many">
      <f:selectItems value="#{demo.manyItems}" />
    </t:selectManyListbox>

    <t:selectManyListbox height="85" value="#{demo.phoneProtocols[0]}" id="protocols">
      <f:selectItems value="#{demo.phoneProtocolItems}" />
    </t:selectManyListbox>

    <t:button action="submit" id="submit-button" label="#{bundle.submit}" />

  </t:page>

</f:view>
