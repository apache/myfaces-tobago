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
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%-- !!!!!! no <f:subview here !!!!!!! --%>
<f:facet name="menuBar">
  <tc:menuBar>
    <tc:menu label="#{overviewBundle.menu_navigate}">
      <tc:menuItem action="mini-howto/intro" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.intro}"/>
      <tc:menuItem action="mini-howto/navigationRules" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.navigationRules}"/>
      <tc:menuItem action="mini-howto/classDefinition" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.classDefinition}"/>
      <tc:menuItem action="mini-howto/jspDefinition" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.jspDefinition}"/>
      <tc:menuItem action="mini-howto/theme" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.theme}"/>
      <tc:menuItem action="mini-howto/validation" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.validation}"/>
      <tc:menuItem action="mini-howto/i18n" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.i18n}"/>

      <%--
                  <tc:menuSeparator />
      FIXME: this doesn't work
                  <tc:menuItem link="/overview/intro.view" 
                                   label="#{miniHowtoBundle.overview}" />
      --%>
    </tc:menu>


    <tc:menu label="#{overviewBundle.menu_config}">
      <tc:menu label="#{overviewBundle.menu_themes}">
        <tx:menuRadio value="#{clientConfigController.theme}"
                     action="#{clientConfigController.submit}" >
          <f:selectItems value="#{clientConfigController.themeItems}"/>
        </tx:menuRadio>
      </tc:menu>
      <tc:menu label="#{overviewBundle.menu_locale}">
        <tx:menuRadio value="#{clientConfigController.locale}"
                     action="#{clientConfigController.submit}">
          <f:selectItems value="#{clientConfigController.localeItems}"/>
        </tx:menuRadio>
      </tc:menu>
      <%-- <tc:menuCheckbox action="#{clientConfigController.submit}"
     label="#{overviewBundle.menu_debug}"
     value="#{clientConfigController.debugMode}" />--%>
    </tc:menu>
  </tc:menuBar>

</f:facet>
