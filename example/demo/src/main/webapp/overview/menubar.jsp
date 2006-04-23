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
<%-- !!!!!! no <f:subview here !!!!!!! --%>
      <f:facet name="menuBar" >
        <tc:menuBar >
          <tc:menu label="#{overviewBundle.menu_navigate}">
            <tc:menuItem action="overview/intro" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.intro}" />
            <tc:menuItem action="overview/basicControls" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.basicControls}"/>
            <tc:menuItem action="overview/sheetControl" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.sheetControl}" />
            <tc:menuItem action="overview/treeControl" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.treeControl}" />
            <tc:menuItem action="overview/tabControl" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.tabControl}" />
            <tc:menuItem action="overview/toolbar" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.toolbar}" />
            <tc:menuItem action="overview/validation" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.validation}" />
            <tc:menuItem action="overview/themes" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.themes}" />
            <tc:menuItem action="overview/browser" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.browser}" />
              <tc:menuSeparator />
            <tc:menuItem action="overview/locale" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.locale}" />
            <tc:menuItem action="overview/layout" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.layout}" />

<%--
            <tc:menuSeparator />
FIXME: this doesn't work
            <tc:menuItem action="#{miniHowtoNavigation.currentPage}"
                type="navigate" label="#{overviewBundle.miniHowto}" />
--%>
          </tc:menu>
          <tc:menu label="#{overviewBundle.menu_config}" >
            <tc:menu label="#{overviewBundle.menu_themes}" >
              <tc:menuRadio value="#{clientConfigController.theme}"
                  action="#{clientConfigController.submit}">
                <f:selectItems value="#{clientConfigController.themeItems}" />
              </tc:menuRadio>
            </tc:menu>
            <tc:menu label="#{overviewBundle.menu_locale}">
              <tc:menuRadio value="#{clientConfigController.locale}"
                  action="#{clientConfigController.submit}">
                <f:selectItems value="#{clientConfigController.localeItems}" />
              </tc:menuRadio>
            </tc:menu>
           <%-- <tc:menuCheckbox action="#{clientConfigController.submit}"
                label="#{overviewBundle.menu_debug}"
                value="#{clientConfigController.debugMode}" />--%>
          </tc:menu>
        </tc:menuBar>

      </f:facet>
