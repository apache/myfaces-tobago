<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:subview id="link_jsp" >

  <t:box label="Links">
    <f:facet name="layout" >
      <t:gridLayout columns="1*;2*" />
    </f:facet>

    <t:label value="Submit Link" />
    <t:link action="#{demo.clickLink}" label="#{bundle.linkClickme}" />

    <t:label value="Reset Link" />
    <t:link type="reset" label="#{bundle.linkClickme}" />

    <t:label value="Navigation Link" />
    <t:link action="/" type="navigate" label="#{bundle.linkClickme}" labelWithAccessKey="te_st" />

    <t:label value="External Link" />
    <t:link action="http://www.atanion.com" type="navigate" label="#{bundle.linkClickme}" />

    <t:label value="Image Link" />
    <t:link action="#{demo.clickLinkImage}"
        image="image/atanion_small.gif" />

  </t:box>

  <t:box label="Buttons">
    <f:facet name="layout" >
      <t:gridLayout columns="1*;2*" />
    </f:facet>

    <t:label value="Submit Button" />
    <t:button action="#{demo.clickButton}" label="#{bundle.linkClickme}" />

    <t:label value="Reset Button" />
    <t:button type="reset" label="#{bundle.linkClickme}" />

    <t:label value="Navigation Button" />
    <t:button action="/" type="navigate" label="#{bundle.linkClickme}" />

    <t:label value="External Button" />
    <t:button action="http://www.atanion.com" type="navigate" label="#{bundle.linkClickme}" />

    <t:label value="Text Button Disabled" />
    <t:button action="#{demo.clickButton}" disabled="true" label="#{bundle.linkClickme}" />

    <t:label value="Image Button" />
    <t:button action="#{demo.clickButtonImage}">
      <t:image value="image/atanion_small.gif"
          alt="atanion" width="200" height="14"
    /></t:button>

  </t:box>

    <t:box label="Menubar"  >

      <t:menuBar >

        <t:menu label="Menu 1" >

          <t:menuItem type="script" action="alert('action 1')"  label="alert 1" />

          <t:menuItem action="/" type="navigate" label="#{bundle.linkClickme}" image="image/config.gif" >
            <f:facet name="confirmation"><t:out value="Do you really want leave this demo?" /></f:facet>
          </t:menuItem>

          <t:menuItem action="http://www.atanion.com" type="navigate" disabled="true" label="disabled" />

          <t:menucheck action="#{demo.clickButton}"  label="#{bundle.linkClickme}" value="#{demo.boolTest}" />
        </t:menu>
        <t:menu label="Menu 2">

          <t:menuItem type="script" action="alert('action 1')"  label="alert 1" />

          <t:menu label="Menu 3">

            <t:menuItem type="script" action="alert('action 1')"  label="alert 1" image="image/date.gif" />

            <t:menuItem action="/" type="navigate" label="#{bundle.linkClickme}" image="image/config.gif" >
              <f:facet name="confirmation"><t:out value="Do you really want leave this demo?" /></f:facet>
            </t:menuItem>

            <t:menuItem action="http://www.atanion.com" type="navigate" disabled="true" label="#{bundle.linkClickme}" image="image/remove.gif" />

          <t:menu label="#{bundle.selectSingleselect}">

            <t:menuradio value="#{demo.salutation[0]}" >
              <f:selectItems value="#{demo.salutationItems}" />
            </t:menuradio>

          </t:menu>

            <t:menuItem action="#{demo.clickButton}"  label="#{bundle.linkClickme}" />
          </t:menu>

          <t:menuSeparator/>

          <t:menucheck label="#{bundle.linkClickme}" value="#{demo.bool[0]}" />
          <t:menucheck label="#{bundle.linkClickme}" value="#{demo.bool[1]}" />
          <t:menucheck label="#{bundle.linkClickme}" value="#{demo.bool[2]}" />

        </t:menu>

      </t:menuBar>


<%--      <t:selectBooleanCheckbox label="#{bundle.solarSaturn}" value="#{demo.boolTest}" id="bool0"  disabled="true"/>--%>

    </t:box>



  <t:box label="Inline">

    You may also put a
    <t:link action="#{demo.clickLinkInline}" label="link" />&nbsp;
    or a
    <t:button action="#{demo.clickButtonInline}" label="button" inline="true" />&nbsp;
    inline in a text.
  </t:box>

  <t:box label="#{bundle.linkOutputbox}">
    <f:facet name="layout" >
      <t:gridLayout />
    </f:facet>
    <t:in value="#{demo.counter}" label="#{bundle.linkCounter}" readonly="true" />
    <t:in value="#{demo.lastCommand}" label="#{bundle.linkLastCommand}" readonly="true" />
    <t:in value="#{demo.text[0]}" label="#{bundle.linkExample}" />
  </t:box>

</f:subview>
