<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="navigator_jsp" >
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:link action="/welcome.view" type="navigate"
        label="#{bundle.nav_welcome}" />

    <t:panel>
      <f:verbatim>
        <br />
        <h5>
      </f:verbatim>
      <t:out value="#{bundle.nav_demoControl}" inline="false"/>
      <f:verbatim>
        </h5>
      </f:verbatim>
    </t:panel>

    <t:link action="/text.view" type="navigate"
        label="#{bundle.nav_text}" />

    <t:link action="/link.view" type="navigate"
        label="#{bundle.nav_link}" />

    <t:link action="/checkbox.view" type="navigate"
        label="#{bundle.nav_checkbox}" />

    <t:link action="/select.view" type="navigate"
        label="#{bundle.nav_selection}" />

    <t:link action="/sheet.view" type="navigate"
        label="#{bundle.nav_sheet}" />

    <t:link action="/layout.view" type="navigate"
        label="#{bundle.nav_layout}" />

    <t:link action="/tab.view" type="navigate"
        label="#{bundle.nav_tab}" />

    <t:link action="/utils.view" type="navigate"
        label="#{bundle.nav_util}" />

    <t:link action="/object.view" type="navigate"
        label="#{bundle.nav_object}" />

    <t:link action="/file.view" type="navigate"
        label="#{bundle.nav_file}" />

    <t:link action="/toolbar.view" type="navigate"
        label="#{bundle.nav_toolbar}" />

    <t:link action="/tree.view" type="navigate"
        label="#{bundle.nav_tree}" />

    <t:link action="/treeListbox.view" type="navigate"
        label="#{bundle.nav_treeListbox}" />

    <t:link action="/validation.view" type="navigate"
        label="#{bundle.nav_validation}" />

    <t:link action="/editor.view" type="navigate"
        label="#{bundle.nav_editor}" />

    <t:panel>
      <f:verbatim>
      <br />
      <h5>
      </f:verbatim>
      <t:out value="#{bundle.nav_applicationDemo}"  inline="false" />
      <f:verbatim>
        </h5>
      </f:verbatim>
    </t:panel>

    <t:link action="/builder/index.view" type="navigate"
        label="#{bundle.nav_builder}" />

    <t:link action="/banking.view" type="navigate"
        label="#{bundle.nav_banking}" />

    <t:link action="/quiz.view" type="navigate"
        label="#{bundle.nav_quiz}" />

    <t:link action="/tutorial.view" type="navigate"
        label="#{bundle.nav_tutorial}" />

<%-- TODO i18n --%>
    <t:link action="/screenshot.view" type="navigate"
        label="Screenshot" />

  </t:panel>
</f:subview>
