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
<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:subview id="navigator" >
  <tc:panel>
    <f:facet name="layout">
      <tc:gridLayout columns="16px;1*"
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
      <tc:image value="image/navigate-pointer.gif"
        width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/intro'}"  />
    </f:verbatim>
    <tc:link action="overview/intro" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.intro}" />

    <f:verbatim>
      <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/basicControls'}" />
    </f:verbatim>
    <tc:link action="overview/basicControls" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.basicControls}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/sheetControl'}" />
    </f:verbatim>
    <tc:link action="overview/sheetControl" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.sheetControl}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/treeControl'}" />
    </f:verbatim>
    <tc:link action="overview/treeControl" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.treeControl}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/tabControl'}" />
    </f:verbatim>
    <tc:link action="overview/tabControl" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.tabControl}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/toolbar'}" />
    </f:verbatim>
    <tc:link action="overview/toolbar" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.toolbar}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/validation'}" />
    </f:verbatim>
    <tc:link action="overview/validation" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.validation}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/form'}" />
    </f:verbatim>
    <tc:link action="overview/form" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.form}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/themes'}" />
    </f:verbatim>
    <tc:link action="overview/themes" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.themes}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/browser'}" />
    </f:verbatim>
    <tc:link action="overview/browser" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.browser}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/locale'}" />
    </f:verbatim>
    <tc:link action="overview/locale" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.locale}" />

    <f:verbatim>
        <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
        rendered="#{overviewNavigation.currentPage == 'overview/layout'}" />
    </f:verbatim>
    <tc:link action="overview/layout" immediate="true"
        actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.layout}" />

    <tc:cell spanX="2">
      <f:verbatim>
        <hr width="80%"/>
      </f:verbatim>
    </tc:cell>

    <tc:cell />
    <tc:link action="#{miniHowtoNavigation.getCurrentPage}" immediate="true"
      tip="#{overviewBundle.miniHowtoNavigateTooltip}"
      label="#{overviewBundle.miniHowto}" />

    <tc:cell spanX="2" />

  </tc:panel>
</f:subview>
