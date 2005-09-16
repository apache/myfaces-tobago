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
<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:subview id="navigator" >
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="16px;1*"
         rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*" />
<%--         rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*" />--%>
    </f:facet>

    <%
      // fixme: this hotfix ensures that, "overviewNavigation" is available
      // fixme: for JSTL-Tags <c:if>
      FacesContext facesContext = FacesContext.getCurrentInstance();
      facesContext.getApplication().getVariableResolver()
          .resolveVariable(facesContext, "overviewNavigation");
    %>

    <f:verbatim>
      <t:image value="image/navigate-pointer.gif"
        width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/intro'}"  />
    </f:verbatim>
    <t:link action="overview/intro" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.intro}" />

    <f:verbatim>
      <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/basicControls'}" />
    </f:verbatim>
    <t:link action="overview/basicControls" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.basicControls}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/sheetControl'}" />
    </f:verbatim>
    <t:link action="overview/sheetControl" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.sheetControl}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/treeControl'}" />
    </f:verbatim>
    <t:link action="overview/treeControl" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.treeControl}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/tabControl'}" />
    </f:verbatim>
    <t:link action="overview/tabControl" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.tabControl}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/toolbar'}" />
    </f:verbatim>
    <t:link action="overview/toolbar" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.toolbar}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/validation'}" />
    </f:verbatim>
    <t:link action="overview/validation" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.validation}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/form'}" />
    </f:verbatim>
    <t:link action="overview/form" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.form}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/themes'}" />
    </f:verbatim>
    <t:link action="overview/themes" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.themes}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/browser'}" />
    </f:verbatim>
    <t:link action="overview/browser" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.browser}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/locale'}" />
    </f:verbatim>
    <t:link action="overview/locale" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.locale}" />

    <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/layout'}" />
    </f:verbatim>
    <t:link action="overview/layout" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.layout}" />

<%--
     <f:verbatim>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/download'}" />
    </f:verbatim>
    <t:link action="overview/download" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.download}" />
--%>

    <t:cell spanX="2">
      <f:verbatim>
        <hr width="80%"/>
      </f:verbatim>
    </t:cell>

    <t:cell />
    <t:link action="#{miniHowtoNavigation.getCurrentPage}" immediate="true"
      tip="#{overviewBundle.miniHowtoNavigateTooltip}"
      label="#{overviewBundle.miniHowto}" />

    <t:cell spanX="2" />

  </t:panel>
</f:subview>
