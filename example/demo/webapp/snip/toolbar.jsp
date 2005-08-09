<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:subview id="toolbar_jsp" >


    <t:box label="Toolbar: small icons, label's on right">
      <f:facet name="toolBar">
        <t:toolBar >

          <t:toolBarCommand type="script" action="alert('action 0')"
              image="image/tobago-richtext-preview.gif" accessKey="a" />

          <t:toolBarCommand type="script" action="alert('action 1')"
              labelWithAccessKey="a_lert 1" image="image/tobago-richtext-edit.gif" />

          <t:toolBarCommand action="/" type="navigate"
              label="#{bundle.linkClickme}">
            <f:facet name="confirmation"><t:out value="Do you really want leave this demo?" /></f:facet>
          </t:toolBarCommand>

        <t:toolBarCheck action="#{clientConfigController.submit}"
                        labelWithAccessKey="#{tobagoBundle.configDebugMode}"
                        value="#{clientConfigController.debugMode}" />

          <t:toolBarCommand action="http://www.atanion.com" type="navigate"
              disabled="true" label="#{bundle.linkClickme}" />

          <t:toolBarCommand action="#{demo.clickButton}"
              label="#{bundle.linkClickme}" />

        </t:toolBar>
      </f:facet>

      <t:toolBar labelPosition="right" >

        <t:toolBarCommand type="script" action="alert('action 0')"
             image="image/tobago-richtext-preview.gif" />

        <t:toolBarCommand type="script" action="alert('action 1')"
              label="alert 1" image="image/tobago-richtext-edit.gif" />

        <t:toolBarCommand action="/" type="navigate"
            label="#{bundle.linkClickme}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand action="http://www.atanion.com" type="navigate"
            disabled="true" label="#{bundle.linkClickme}" />

        <t:toolBarCommand action="#{demo.clickButton}"
            label="#{bundle.linkClickme}" />


        <t:toolBarCheck action="#{clientConfigController.submit}"
                        labelWithAccessKey="#{tobagoBundle.configDebugMode}"
                        value="#{clientConfigController.debugMode}" />

      </t:toolBar>
    </t:box>





    <t:box label="Toolbar: small icons, label's on bottom">

      <t:toolBar  >

        <t:toolBarCommand type="script" action="alert('action 0')"
             image="image/tobago-richtext-preview.gif" />

        <t:toolBarCommand type="script" action="alert('action 1')"
              label="alert 1" image="image/tobago-richtext-edit.gif" />

        <t:toolBarCommand action="/" type="navigate"
            label="#{bundle.linkClickme}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand action="http://www.atanion.com" type="navigate"
            disabled="true" label="#{bundle.linkClickme}" />

        <t:toolBarCommand action="#{demo.clickButton}"
            label="#{bundle.linkClickme}" />

      </t:toolBar>
    </t:box>





    <t:box label="Toolbar: small icons, no label's">
      <f:facet name="toolBar">
        <t:toolBar labelPosition="off" >

          <t:toolBarCommand type="script" action="alert('action 0')"
              image="image/tobago-richtext-preview.gif" accessKey="a" />

          <t:toolBarCommand type="script" action="alert('action 1')"
              labelWithAccessKey="a_lert 1" image="image/tobago-richtext-edit.gif" />

          <t:toolBarCommand action="/" type="navigate"
              label="#{bundle.linkClickme}">
            <f:facet name="confirmation"><t:out value="Do you really want leave this demo?" /></f:facet>
          </t:toolBarCommand>

          <t:toolBarCommand action="http://www.atanion.com" type="navigate"
              disabled="true" label="#{bundle.linkClickme}" />

          <t:toolBarCommand action="#{demo.clickButton}"
              label="#{bundle.linkClickme}" />

        </t:toolBar>
      </f:facet>

      <t:toolBar  labelPosition="off" >

        <t:toolBarCommand type="script" action="alert('action 0')"
             image="image/tobago-richtext-preview.gif" />

        <t:toolBarCommand type="script" action="alert('action 1')"
              label="alert 1" image="image/tobago-richtext-edit.gif" />

        <t:toolBarCommand action="/" type="navigate"
            label="#{bundle.linkClickme}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand action="http://www.atanion.com" type="navigate"
            disabled="true" label="#{bundle.linkClickme}" />

        <t:toolBarCommand action="#{demo.clickButton}"
            label="#{bundle.linkClickme}" />

        <t:toolBarSelectOne value="#{demo.text[0]}" >
         <t:selectItem itemLabel="next" itemDescription="next value" itemValue="next" itemImage="image/next.gif"/>
         <t:selectItem itemLabel="prev" itemDescription="previous value" itemValue="prev" itemImage="image/prev.gif" />
        </t:toolBarSelectOne>

      </t:toolBar>
    </t:box>





    <t:box label="Toolbar: no icons, label's on bottom">
      <f:facet name="toolBar">
        <t:toolBar iconSize="off" >

          <t:toolBarCommand type="script" action="alert('action 0')"
              image="image/tobago-richtext-preview.gif" accessKey="a" />

          <t:toolBarCommand type="script" action="alert('action 1')"
              labelWithAccessKey="a_lert 1" image="image/tobago-richtext-edit.gif" />

          <t:toolBarCommand action="/" type="navigate"
              label="#{bundle.linkClickme}">
            <f:facet name="confirmation"><t:out value="Do you really want leave this demo?" /></f:facet>
          </t:toolBarCommand>

          <t:toolBarCommand action="http://www.atanion.com" type="navigate"
              disabled="true" label="#{bundle.linkClickme}" />

          <t:toolBarCommand action="#{demo.clickButton}"
              label="#{bundle.linkClickme}" />

        </t:toolBar>
      </f:facet>

      <t:toolBar iconSize="off"  >

        <t:toolBarCommand type="script" action="alert('action 0')"
             image="image/tobago-richtext-preview.gif" />

        <t:toolBarCommand type="script" action="alert('action 1')"
              label="alert 1" image="image/tobago-richtext-edit.gif" />

        <t:toolBarCommand action="/" type="navigate"
            label="#{bundle.linkClickme}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand action="http://www.atanion.com" type="navigate"
            disabled="true" label="#{bundle.linkClickme}" />

        <t:toolBarCommand action="#{demo.clickButton}"
            label="#{bundle.linkClickme}" />

        <t:menuradio value="#{clientConfigController.theme}"
                          action="#{clientConfigController.submit}" >
          <f:selectItems value="#{clientConfigController.themeItems}" />
        </t:menuradio>

      </t:toolBar>
    </t:box>


    <t:box label="Toolbar: big icons, label's on right">

      <t:toolBar iconSize="big" labelPosition="right" >

        <t:toolBarCommand type="script" action="alert('action 0')"
             image="image/tobago-richtext-preview.gif" label="test text" />

        <t:toolBarCommand type="script" action="alert('action 1')"
              label="alert 1" image="image/tobago-richtext-edit.gif" />

        <t:toolBarCommand action="/" type="navigate"
            label="#{bundle.linkClickme}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand action="http://www.atanion.com" type="navigate"
            disabled="true" label="#{bundle.linkClickme}" />

      </t:toolBar>
    </t:box>


    <t:box label="Toolbar: big icons, label's on bottom">

      <t:toolBar iconSize="big" >

        <t:toolBarCommand type="script" action="alert('action 0')"
             image="image/tobago-richtext-preview.gif" />

        <t:toolBarCommand type="script" action="alert('action 1')"
              label="alert 1" image="image/tobago-richtext-edit.gif" />

        <t:toolBarCommand action="/" type="navigate"
            label="#{bundle.linkClickme}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand action="http://www.atanion.com" type="navigate"
            disabled="true" label="#{bundle.linkClickme}" />

        <t:toolBarCommand action="#{demo.clickButton}"
            label="#{bundle.linkClickme}" />

      </t:toolBar>
    </t:box>


</f:subview>
