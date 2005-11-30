<%--
 * Copyright 2002-2005 The Apache Software Foundation.
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
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
<jsp:body>

<tc:panel>
  <f:facet name="layout">
    <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;fixed;
                        fixed;fixed;fixed;fixed;1*"/>

  </f:facet>

  <tc:link type="navigate" action="box.jsp" immediate="true" label="Box" />

  <tc:link type="navigate" action="button.jsp" immediate="true" label="Button" />

  <tc:link type="navigate" action="date.jsp" immediate="true" label="Date" />

  <tc:link type="navigate" action="calendar.jsp" immediate="true" label="Calendar" />

  <tc:link type="navigate" action="file.jsp" immediate="true" label="File" />

  <tc:link type="navigate" action="image.jsp" immediate="true" label="Image" />

  <tc:link type="navigate" action="in.jsp" immediate="true" label="In" />

  <tc:link type="navigate" action="label.jsp" immediate="true" label="Label" />

  <tc:link type="navigate" action="link.jsp" immediate="true" label="Link" />

  <tc:link type="navigate" action="menuBar.jsp" immediate="true" label="Menubar" />

  <tc:link type="navigate" action="menucheck.jsp" immediate="true" label="Menucheck" />

  <tc:link type="navigate" action="menuradio.jsp" immediate="true" label="Menuradio" />

  <tc:link type="navigate" action="messages.jsp" immediate="true" label="Messages" />

  <tc:link type="navigate" action="out.jsp" immediate="true" label="Out" />

  <tc:link type="navigate" action="progress.jsp" immediate="true" label="Progress" />

  <%--<tc:link type="navigate" action="richTextEditor.jsp" immediate="true" label="RichTextEditor" />--%>

  <tc:link type="navigate" action="selectBooleanCheckbox.jsp" immediate="true" label="SelectBooleanCheckbox" />

  <tc:link type="navigate" action="selectManyListBox.jsp" immediate="true" label="SelectManyListbox" />

  <tc:link type="navigate" action="selectOneChoice.jsp" immediate="true" label="SelectOneChoice" />

  <tc:link type="navigate" action="selectOneListbox.jsp" immediate="true" label="SelectOneListbox" />

  <tc:link type="navigate" action="sheet.jsp" immediate="true" label="Sheet" />

  <%--<tc:link type="navigate" action="tabGroup.jsp" immediate="true" label="TabGroup" />--%>

  <tc:link type="navigate" action="textarea.jsp" immediate="true" label="Textarea" />

  <tc:link type="navigate" action="toolBar.jsp" immediate="true" label="ToolBar" />

  <%--<tc:link type="navigate" action="toolBarCheck.jsp" immediate="true" label="ToolBarCheck" />--%>

  <%--<tc:link type="navigate" action="toolBarSelectOne.jsp" immediate="true" label="TooBarSelectOne" />--%>

  <tc:link type="navigate" action="tree.jsp" immediate="true" label="Tree" />

  <tc:link type="navigate" action="treeListBox.jsp" immediate="true" label="TreeListBox" />


  <tc:cell/>

</tc:panel>

</jsp:body>
</layout:screenshot>
