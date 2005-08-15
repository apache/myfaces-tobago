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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<f:subview id="navigator" >
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="10px;1*"
         rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*" />
    </f:facet>

    <%
      // fixme: this hotfix ensures that, "miniHowtoNavigation" is available
      // for JSTL-Tags
      FacesContext facesContext = FacesContext.getCurrentInstance();
      facesContext.getApplication().getVariableResolver()
          .resolveVariable(facesContext, "miniHowtoNavigation");
    %>

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/intro"}'>
        <t:image value="image/navigate-pointer.gif" height="16" width="16" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/intro" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.intro}" />

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/jspDefinition"}'>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/jspDefinition" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.jspDefinition}" />

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/navigationRules"}'>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/navigationRules" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.navigationRules}" />

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/classDefinition"}'>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/classDefinition" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.classDefinition}" />

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/theme"}'>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/theme" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.theme}" />

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/validation"}'>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/validation" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.validation}" />

    <f:verbatim>
      <c:if test='${miniHowtoNavigation.currentPage == "mini-howto/i18n"}'>
        <t:image value="image/navigate-pointer.gif" width="16px" height="16px" />
      </c:if>
    </f:verbatim>
    <t:link action="mini-howto/i18n" immediate="true"
        actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.i18n}" />

    <t:cell spanX="2">
      <f:verbatim>
        <hr width="80%"/>
      </f:verbatim>
    </t:cell>

    <t:cell />
    <t:link action="#{overviewNavigation.getCurrentPage}" immediate="true"
      tip="#{miniHowtoBundle.overviewNavigateTooltip}"
      label="#{miniHowtoBundle.overview}" />

    <t:cell spanX="2"  />

  </t:panel>
</f:subview>
