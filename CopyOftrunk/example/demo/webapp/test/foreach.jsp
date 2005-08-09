<%@ page import="java.util.ArrayList,
                 java.util.List,
                 java.util.HashMap,
                 java.util.Map"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.atanion.com/taglib/logging" prefix="log"%>
<log:factory var="LOG" />

<%
  final Object attribute = session.getAttribute("rowModel");
  if (attribute != null) {%><log:info var="LOG" message="<%= "rowModel ist " + attribute.getClass().getName() %>" /><%};
  Object[] model = (Object[]) attribute;
  if (model == null) {
    %>  <log:info var="LOG" message="kein sheet gefunden erzeuge neues" /><%
    model = new Object[3];
    model[0] =  new String[][] {
      {"aaaaaa", "15", "0.0"},
      {"bbbbbb", "14", "1.5"},
      {"cccccc", "13", "1.0"}
    };

    model[1] = new ArrayList();
    ((List)model[1]).add(new String[] {"dddddd", "12", "0.1"});
    ((List)model[1]).add(new String[] {"eeeeee", "11", "1.4"});
    ((List)model[1]).add(new String[] {"ffffff", "10", "0.9"});

    model[2] = new HashMap();
    ((Map)model[2]).put("key1", new String[] {"gggggg", "9", "0.2"});
    ((Map)model[2]).put("key2", new String[] {"hhhhhh", "8", "1.3"});
    ((Map)model[2]).put("key3", new String[] {"iiiiii", "7", "0.8"});
    ((Map)model[2]).put("key4", new String[] {"jjjjjj", "6", "0.3"});
    session.setAttribute("rowModel", model);
  }
%>
<f:view>
  <t:page label="test" id="page">
  <t:box width="300" height="400">
   <f:facet name="layout" >
     <t:gridLayout rows="fixed;fixed;fixed;5px;fixed;fixed;fixed;5px;fixed;fixed;fixed;fixed;1*;fixed" columns="2*;1*;1*"  />
   </f:facet>
    <t:forEach items="#{rowModel[0]}" var="data" >
      <t:in value="#{data[0]}" />
      <t:out value="#{data[1]}" />
      <t:out value="#{data[2]}" />
    </t:forEach>
    <t:cell spanX="3"><hr /></t:cell>
    <t:forEach items="#{rowModel[1]}" var="data" >
      <t:in value="#{data[0]}" />
      <t:out value="#{data[1]}" />
      <t:out value="#{data[2]}" />
    </t:forEach>
    <t:cell spanX="3"><hr /></t:cell>
    <t:forEach items="#{rowModel[2]}" var="data" >
      <t:in value="#{data[0]}" />
      <t:out value="#{data[1]}" />
      <t:out value="#{data[2]}" />
    </t:forEach>
    <t:cell spanX="3"  />
    <t:button id="submit-button" />
    <t:cell spanX="2"  />
  </t:box>

  </t:page>
</f:view>
