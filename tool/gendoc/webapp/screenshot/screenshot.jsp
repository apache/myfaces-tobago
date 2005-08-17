<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
<jsp:body>

<t:panel>
  <f:facet name="layout">
    <t:gridLayout rows="fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;1*"/>

  </f:facet>

  <t:link type="navigate" action="box.jsp" immediate="true" label="Box" />

  <t:link type="navigate" action="button.jsp" immediate="true" label="Button" />

  <t:link type="navigate" action="date.jsp" immediate="true" label="Date" />

  <t:link type="navigate" action="calendar.jsp" immediate="true" label="Calendar" />

  <t:link type="navigate" action="file.jsp" immediate="true" label="File" />

  <t:link type="navigate" action="image.jsp" immediate="true" label="Image" />

  <t:link type="navigate" action="in.jsp" immediate="true" label="In" />

  <t:link type="navigate" action="label.jsp" immediate="true" label="Label" />

  <t:link type="navigate" action="link.jsp" immediate="true" label="Link" />

  <t:link type="navigate" action="menuBar.jsp" immediate="true" label="Menubar" />

  <t:link type="navigate" action="menucheck.jsp" immediate="true" label="Menucheck" />

  <t:link type="navigate" action="menuradio.jsp" immediate="true" label="Menuradio" />

  <t:link type="navigate" action="messages.jsp" immediate="true" label="Messages" />

  <t:link type="navigate" action="out.jsp" immediate="true" label="Out" />

  <t:link type="navigate" action="progress.jsp" immediate="true" label="Progress" />

  <t:link type="navigate" action="richTextEditor.jsp" immediate="true" label="RichTextEditor" />

  <t:link type="navigate" action="selectBooleanCheckbox.jsp" immediate="true" label="SelectBooleanCheckbox" />

  <t:link type="navigate" action="selectManyListBox.jsp" immediate="true" label="SelectManyListbox" />

  <t:link type="navigate" action="selectOneChoice.jsp" immediate="true" label="SelectOneChoice" />

  <t:link type="navigate" action="selectOneListbox.jsp" immediate="true" label="SelectOneListbox" />

  <t:link type="navigate" action="sheet.jsp" immediate="true" label="Sheet" />

  <t:link type="navigate" action="tabGroup.jsp" immediate="true" label="TabGroup" />

  <t:link type="navigate" action="textarea.jsp" immediate="true" label="Textarea" />

  <t:link type="navigate" action="toolBar.jsp" immediate="true" label="TooBar" />

  <t:link type="navigate" action="toolBarCheck.jsp" immediate="true" label="ToolBarCheck" />

  <t:link type="navigate" action="toolBarSelectOne.jsp" immediate="true" label="TooBarSelectOne" />

  <t:link type="navigate" action="tree.jsp" immediate="true" label="Tree" />

  <t:link type="navigate" action="treeListBox.jsp" immediate="true" label="TreeListBox" />


  <t:cell/>

</t:panel>

</jsp:body>
</layout:screenshot>
