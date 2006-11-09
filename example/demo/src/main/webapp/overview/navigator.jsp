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
      <tc:gridLayout columns="16px;1*"
                     rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*"/>
      <%--         rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*" />--%>
    </f:facet>

    <tc:image value="image/navigate-pointer.gif"
              width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/intro'}"/>
    <tc:link action="overview/intro" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.intro}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/basicControls'}"/>
    <tc:link action="overview/basicControls" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.basicControls}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/sheetControl'}"/>
    <tc:link action="overview/sheetControl" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.sheetControl}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/treeControl'}"/>
    <tc:link action="overview/treeControl" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.treeControl}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/tabControl'}"/>
    <tc:link action="overview/tabControl" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.tabControl}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/toolbar'}"/>
    <tc:link action="overview/toolbar" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.toolbar}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/validation'}"/>
    <tc:link action="overview/validation" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.validation}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/form'}"/>
    <tc:link action="overview/form" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.form}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/themes'}"/>
    <tc:link action="overview/themes" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.themes}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/browser'}"/>
    <tc:link action="overview/browser" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.browser}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/locale'}"/>
    <tc:link action="overview/locale" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.locale}"/>

    <tc:image value="image/navigate-pointer.gif" width="16px" height="16px"
              rendered="#{overviewNavigation.currentPage == 'overview/layout'}"/>
    <tc:link action="overview/layout" immediate="true"
             actionListener="#{overviewNavigation.navigate}" label="#{overviewBundle.layout}"/>

    <tc:cell spanX="2">
      <f:verbatim>
        <hr width="80%"/>
      </f:verbatim>
    </tc:cell>

    <tc:cell/>
    <tc:link action="#{miniHowtoNavigation.getCurrentPage}" immediate="true"
             tip="#{overviewBundle.miniHowtoNavigateTooltip}"
             label="#{overviewBundle.miniHowto}"/>

    <tc:cell spanX="2"/>

  </tc:panel>
</f:subview>
