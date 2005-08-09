<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%-- !!!!!! no <f:subview here !!!!!!! --%>
      <f:facet name="menuBar" >
        <t:menuBar >
          <t:menu label="Navigation" accessKey="n" >
            <t:menuItem label="#{bundle.nav_text}"
                             action="/text.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_link}"
                             action="/link.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_checkbox}"
                             action="/checkbox.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_selection}"
                             action="/select.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_sheet}"
                             action="/sheet.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_layout}"
                             action="/layout.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_tab}"
                             action="/tab.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_util}"
                             action="/utils.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_object}"
                             action="/object.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_file}"
                             action="/file.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_tree}"
                             action="/tree.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_treeListbox}"
                             action="/treeListbox.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_validation}"
                             action="/validation.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_editor}"
                             action="/editor.view" type="navigate"/>

            <t:menuSeparator />

            <t:menuItem label="#{bundle.nav_builder}"
                             action="/builder/index.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_banking}"
                             action="/banking.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_quiz}"
                             action="/quiz.view" type="navigate"/>
            <t:menuItem label="#{bundle.nav_tutorial}"
                             action="/tutorial.view" type="navigate"/>
          </t:menu>
          <t:menu labelWithAccessKey="_Configuration" >
            <t:menu labelWithAccessKey="#{bundle.configThemeWithAccessKey}" >
              <t:menuradio value="#{clientConfigController.theme}"
                                action="#{clientConfigController.submit}" >
                <f:selectItems value="#{clientConfigController.themeItems}" />
              </t:menuradio>
            </t:menu>
            <t:menu label="#{bundle.configLocale}" >
              <t:menuradio value="#{clientConfigController.locale}"
                                action="#{clientConfigController.submit}" >
                <f:selectItems value="#{clientConfigController.localeItems}" />
              </t:menuradio>
            </t:menu>
            <t:menu label="#{bundle.configContentType}" >
              <t:menuradio value="#{clientConfigController.contentType}"
                                action="#{clientConfigController.submit}" >
                <f:selectItems value="#{clientConfigController.contentTypeItems}" />
              </t:menuradio>
            </t:menu>
            <t:menucheck action="#{clientConfigController.submit}"
                              labelWithAccessKey="#{bundle.configDebugMode}"
                              value="#{clientConfigController.debugMode}" />
          </t:menu>
        </t:menuBar>

      </f:facet>
