<%@ page import="java.util.Date,
                 java.text.SimpleDateFormat"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <t:page label="Hallo" id="page" >

    Inhalt 1


    <t:panel>
    <f:facet name="layout">
      <t:gridLayout border="1" />
    </f:facet>

    <t:out value="Text 2" />

    <t:box label="Hallo 3">
      Inhalt [<%= new SimpleDateFormat("MM:ss").format(new Date()) %>] 4
      <t:out id="i5" value="Text 5" />
      Inhalt 6
      <t:out id="i7" value="Text 7" />
      Inhalt 8
    </t:box>

    <t:out value="Text 9" />

    </t:panel>

    Inhalt 10

    <t:box label="Hallo 11">
      Inhalt [<%= new SimpleDateFormat("MM:ss").format(new Date()) %>] 12
      <t:out id="i13" value="Text 13" />
      Inhalt 14
      <t:out id="i15" value="Text 15" />
      Inhalt 16
    </t:box>

    Inhalt 17

    <t:button label="#{bundle.submit}" />

  </t:page>
</f:view>
