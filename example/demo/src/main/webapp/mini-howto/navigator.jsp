<%--
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="navigator">
  <tc:panel>
    <f:facet name="layout">
      <tc:gridLayout columns="10px;1*"
                     rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*"/>
    </f:facet>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/intro'}"
              value="image/navigate-pointer.gif" height="16" width="16"/>

    <tc:link action="mini-howto/intro" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.intro}"/>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/jspDefinition'}"
              value="image/navigate-pointer.gif" width="16px" height="16px"/>

    <tc:link action="mini-howto/jspDefinition" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.jspDefinition}"/>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/navigationRules'}"
              value="image/navigate-pointer.gif" width="16px" height="16px"/>

    <tc:link action="mini-howto/navigationRules" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.navigationRules}"/>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/classDefinition'}"
              value="image/navigate-pointer.gif" width="16px" height="16px"/>

    <tc:link action="mini-howto/classDefinition" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.classDefinition}"/>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/theme'}"
              value="image/navigate-pointer.gif" width="16px" height="16px"/>

    <tc:link action="mini-howto/theme" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.theme}"/>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/validation'}"
              value="image/navigate-pointer.gif" width="16px" height="16px"/>

    <tc:link action="mini-howto/validation" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.validation}"/>

    <tc:image rendered="#{miniHowtoNavigation.currentPage == 'mini-howto/i18n'}"
              value="image/navigate-pointer.gif" width="16px" height="16px"/>

    <tc:link action="mini-howto/i18n" immediate="true"
             actionListener="#{miniHowtoNavigation.navigate}" label="#{miniHowtoBundle.i18n}"/>

    <tc:cell spanX="2">
      <f:verbatim>
        <hr width="80%"/>
      </f:verbatim>
    </tc:cell>

    <tc:cell/>

    <tc:link action="#{overviewNavigation.getCurrentPage}" immediate="true"
             tip="#{miniHowtoBundle.overviewNavigateTooltip}"
             label="#{miniHowtoBundle.overview}"/>

    <tc:cell spanX="2"/>

  </tc:panel>
</f:subview>
