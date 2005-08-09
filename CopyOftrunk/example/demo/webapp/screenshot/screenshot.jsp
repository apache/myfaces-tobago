<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
<jsp:body>

<t:panel>
  <f:facet name="layout">
    <t:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;1*"/>
  </f:facet>

  <t:link action="screenshot/box" immediate="true" label="Box" />

  <t:link action="screenshot/button" immediate="true" label="Button" />

  <t:link action="screenshot/date" immediate="true" label="Date" />

  <t:link action="screenshot/calendar" immediate="true" label="Calendar" />

  <t:link action="screenshot/file" immediate="true" label="File" />

  <t:link action="screenshot/image" immediate="true" label="Image" />

  <t:link action="screenshot/in" immediate="true" label="In" />

  <t:link action="screenshot/label" immediate="true" label="Label" />

  <t:link action="screenshot/link" immediate="true" label="Link" />

  <t:link action="screenshot/menuBar" immediate="true" label="Menubar" />

  <t:link action="screenshot/menucheck" immediate="true" label="Menucheck" />

  <t:link action="screenshot/menuradio" immediate="true" label="Menuradio" />

  <t:link action="screenshot/messages" immediate="true" label="Messages" />

  <t:link action="screenshot/out" immediate="true" label="Out" />

  <t:link action="screenshot/progress" immediate="true" label="Progress" />

  <t:link action="screenshot/richTextEditor" immediate="true" label="RichTextEditor" />

  <t:link action="screenshot/selectBooleanCheckbox" immediate="true" label="SelectBooleanCheckbox" />

  <t:link action="screenshot/selectManyListBox" immediate="true" label="SelectManyListbox" />

  <t:link action="screenshot/selectOneChoice" immediate="true" label="SelectOneChoice" />

  <t:link action="screenshot/selectOneListbox" immediate="true" label="SelectOneListbox" />

  <t:link action="screenshot/sheet" immediate="true" label="Sheet" />

  <t:link action="screenshot/tabGroup" immediate="true" label="TabGroup" />

  <t:link action="screenshot/textarea" immediate="true" label="Textarea" />

  <t:link action="screenshot/tooBar" immediate="true" label="TooBar" />

  <t:link action="screenshot/toolBarCheck" immediate="true" label="ToolBarCheck" />

  <t:link action="screenshot/tooBarSelectOne" immediate="true" label="TooBarSelectOne" />

  <t:link action="screenshot/tree" immediate="true" label="Tree" />

  <t:link action="screenshot/treeListBox" immediate="true" label="TreeListBox" />


  <t:cell/>

</t:panel>

</jsp:body>
</layout:screenshot>
