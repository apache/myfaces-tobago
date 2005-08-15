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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%-- !!!!!! no <f:subview here !!!!!!! --%>
<f:facet name="menuBar">
  <t:menuBar>
    <t:menu labelWithAccessKey="#{overviewBundle.menu_navigate}">
      <t:menuItem action="mini-howto/intro" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.intro}"/>
      <t:menuItem action="mini-howto/navigationRules" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.navigationRules}"/>
      <t:menuItem action="mini-howto/classDefinition" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.classDefinition}"/>
      <t:menuItem action="mini-howto/jspDefinition" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.jspDefinition}"/>
      <t:menuItem action="mini-howto/theme" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.theme}"/>
      <t:menuItem action="mini-howto/validation" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.validation}"/>
      <t:menuItem action="mini-howto/i18n" immediate="true"
                  actionListener="#{miniHowtoNavigation.navigate}"
                  label="#{miniHowtoBundle.i18n}"/>

      <%--
                  <t:menuSeparator />
      fixme: this doesn't work
                  <t:menuItem action="/overview/intro.view" type="navigate"
                                   label="#{miniHowtoBundle.overview}" />
      --%>
    </t:menu>


    <t:menu labelWithAccessKey="#{overviewBundle.menu_config}">
      <t:menu labelWithAccessKey="#{overviewBundle.menu_themes}">
        <t:menuradio value="#{clientConfigController.theme}"
                     action="#{clientConfigController.submit}"
            >
          <f:selectItems value="#{clientConfigController.themeItems}"/>
        </t:menuradio>
      </t:menu>
      <t:menu labelWithAccessKey="#{overviewBundle.menu_locale}">
        <t:menuradio value="#{clientConfigController.locale}"
                     action="#{clientConfigController.submit}">
          <f:selectItems value="#{clientConfigController.localeItems}"/>
        </t:menuradio>
      </t:menu>
      <%-- <t:menucheck action="#{clientConfigController.submit}"
     label="#{overviewBundle.menu_debug}"
     value="#{clientConfigController.debugMode}" />--%>
    </t:menu>
  </t:menuBar>

</f:facet>
