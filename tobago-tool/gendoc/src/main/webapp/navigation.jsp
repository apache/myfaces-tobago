<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<f:view>

  <tc:loadBundle basename="demo" var="bundle"/>

  <tc:page label="Screenshot" id="page"
           width="200px" height="800px">
    <f:facet name="layout">
      <tc:gridLayout margin="5px"
                     rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*"/>
    </f:facet>

    <tc:link link="screenshot/box.jsp" label="Box" target="View"/>

    <tc:link link="screenshot/button.jsp" label="Button" target="View"/>

    <tc:link link="screenshot/date.jsp"
             label="Date" target="View"/>

    <tc:link link="screenshot/calendar.jsp"
             label="Calendar" target="View"/>

    <tc:link link="screenshot/file.jsp"
             label="File" target="View"/>

    <tc:link link="screenshot/image.jsp"
             label="Image" target="View"/>

    <tc:link link="screenshot/in.jsp" label="In"
             target="View"/>

    <tc:link link="screenshot/label.jsp"
             label="Label" target="View"/>

    <tc:link link="screenshot/link.jsp"
             label="Link" target="View"/>

    <tc:link link="screenshot/menuBar.jsp"
             label="Menubar" target="View"/>

    <tc:link link="screenshot/menucheck.jsp"
             label="Menucheck" target="View"/>

    <tc:link link="screenshot/menuradio.jsp"
             label="Menuradio" target="View"/>

    <tc:link link="screenshot/messages.jsp"
             label="Messages" target="View"/>

    <tc:link link="screenshot/out.jsp" label="Out"
             target="View"/>

    <tc:link link="screenshot/progress.jsp"
             label="Progress" target="View"/>

    <%--<tc:link link="screenshot/richTextEditor.jsp"  label="RichTextEditor"  target="View"/>--%>

    <tc:link link="screenshot/selectBooleanCheckbox.jsp"
             label="SelectBooleanCheckbox" target="View"/>

    <tc:link link="screenshot/selectManyListBox.jsp"
             label="SelectManyListbox" target="View"/>

    <tc:link link="screenshot/selectOneChoice.jsp"
             label="SelectOneChoice" target="View"/>

    <tc:link link="screenshot/selectOneListbox.jsp"
             label="SelectOneListbox" target="View"/>

    <tc:link link="screenshot/sheet.jsp"
             label="Sheet" target="View"/>

    <%--<tc:link link="screenshot/tabGroup.jsp"  label="TabGroup"  target="View"/>--%>

    <tc:link link="screenshot/textarea.jsp"
             label="Textarea" target="View"/>

    <tc:link link="screenshot/toolBar.jsp"
             label="ToolBar" target="View"/>

    <%--<tc:link link="screenshot/toolBarCheck.jsp"  label="ToolBarCheck"  target="View"/>--%>

    <%--<tc:link link="screenshot/toolBarSelectOne.jsp"  label="TooBarSelectOne"  target="View"/>--%>

    <tc:link link="screenshot/tree.jsp"
             label="Tree" target="View"/>

    <tc:link link="screenshot/treeOld.jsp"
             label="Tree Old" target="View"/>

    <tc:link link="screenshot/treeListBox.jsp"
             label="TreeListBox" target="View"/>


    <tc:cell/>
  </tc:page>
</f:view>
