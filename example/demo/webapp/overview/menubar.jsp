<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%-- !!!!!! no <f:subview here !!!!!!! --%>
      <f:facet name="menuBar" >
        <t:menuBar >
          <t:menu labelWithAccessKey="#{overviewBundle.menu_navigate}">
            <t:menuItem action="overview/intro" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.intro}" />
            <t:menuItem action="overview/basicControls" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.basicControls}"/>
            <t:menuItem action="overview/sheetControl" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.sheetControl}" />
            <t:menuItem action="overview/treeControl" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.treeControl}" />
            <t:menuItem action="overview/tabControl" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.tabControl}" />
            <t:menuItem action="overview/toolbar" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.toolbar}" />
            <t:menuItem action="overview/validation" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.validation}" />
            <t:menuItem action="overview/themes" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.themes}" />
            <t:menuItem action="overview/browser" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.browser}" />
            <t:menuItem action="overview/locale" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.locale}" />
            <t:menuItem action="overview/layout" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.layout}" />
           <%-- <t:menuItem action="overview/download" immediate="true"
                             actionListener="#{overviewNavigation.navigate}"
                             label="#{overviewBundle.download}" />--%>

<%--
            <t:menuSeparator />
fixme: this doesn't work
            <t:menuItem action="#{miniHowtoNavigation.currentPage}"
                type="navigate" label="#{overviewBundle.miniHowto}" />
--%>
          </t:menu>
          <t:menu labelWithAccessKey="#{overviewBundle.menu_config}" >
            <t:menu labelWithAccessKey="#{overviewBundle.menu_themes}" >
              <t:menuradio value="#{clientConfigController.theme}"
                  action="#{clientConfigController.submit}">
                <f:selectItems value="#{clientConfigController.themeItems}" />
              </t:menuradio>
            </t:menu>
            <t:menu labelWithAccessKey="#{overviewBundle.menu_locale}">
              <t:menuradio value="#{clientConfigController.locale}"
                  action="#{clientConfigController.submit}">
                <f:selectItems value="#{clientConfigController.localeItems}" />
              </t:menuradio>
            </t:menu>
           <%-- <t:menucheck action="#{clientConfigController.submit}"
                label="#{overviewBundle.menu_debug}"
                value="#{clientConfigController.debugMode}" />--%>
          </t:menu>
        </t:menuBar>

      </f:facet>
