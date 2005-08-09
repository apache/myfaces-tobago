<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel id="pageToolbar" >
      <f:facet name="layout">
        <t:gridLayout rows="80px;fixed;10px;1*;fixed;10px"  id="pageToolbarLayout"/>
<%--        <t:gridLayout rows="80px;1*;10px;100px;1*;10px"  id="pageToolbarLayout"/>--%>
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.toolbar_text1}" />

      <t:box label="#{overviewBundle.toolbar_sampleTitle}" id="boxToolbar" >

        <f:facet name="layout">
          <t:gridLayout rows="fixed;fixed;fixed" columns="2*;1*;1*" id="boxToolbarLayout"/>
        </f:facet>

        <f:facet name="toolBar">
        <t:toolBar>
          <t:toolBarCommand id="button" action="overview/toolbar"
              actionListener="#{overviewController.click}"
              label="#{overviewBundle.toolbar_buttonAction}" />

        <t:toolBarCommand id="imageButton" action="overview/toolbar"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.toolbar_imageButtonAction}"
            image="image/toolbar_example_button.gif" />



        <t:toolBarCommand id="popupButton" action="overview/toolbar"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.toolbar_popupButtonAction}">

          <f:facet name="popup">
            <t:popup width="200" height="100"
                          rendered="#{overviewController.showPopup}" id="popup" >

              <f:facet name="layout">
                <t:gridLayout rows="35px;1*;fixed" />
              </f:facet>

              <t:out value="#{overviewBundle.toolbar_popupText}"/>

              <t:cell/>
              <t:button id="popupCloseButton"
                             actionListener="#{overviewController.click}"
                             label="#{overviewBundle.toolbar_closePopupAction}" />

            </t:popup>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand id="DropDown" action="overview/toolbar"
          actionListener="#{overviewController.click}" label="#{overviewBundle.toolbar_dropDownAction}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem id="dropdown3" actionListener="#{overviewController.click}" label="Action 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        </t:toolBar>
        </f:facet>

        <t:cell spanX="3">
        <t:toolBar iconSize="#{demo.toolbarIconSize}"
            labelPosition="#{demo.toolbarTextPosition}" >
          <t:toolBarCommand id="button2" action="overview/toolbar"
              actionListener="#{overviewController.click}"
              label="#{overviewBundle.toolbar_buttonAction}" />

        <t:toolBarCommand id="imageButton2" action="overview/toolbar"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.toolbar_imageButtonAction}"
            image="image/toolbar_example_button.gif" />



        <t:toolBarCommand id="popupButton2" action="overview/toolbar"
            actionListener="#{overviewController.click}"
            label="#{overviewBundle.toolbar_popupButtonAction}">

          <f:facet name="popup">
            <t:popup width="200" height="100"
                          rendered="#{overviewController.showPopup}" id="popup2" >

              <f:facet name="layout">
                <t:gridLayout rows="35px;1*;fixed" />
              </f:facet>

              <t:out value="#{overviewBundle.toolbar_popupText}"/>

              <t:cell/>
              <t:button id="popupCloseButton2"
                             actionListener="#{overviewController.click}"
                             label="#{overviewBundle.toolbar_closePopupAction}" />

            </t:popup>
          </f:facet>
        </t:toolBarCommand>

        <t:toolBarCommand id="DropDown2" action="overview/toolbar"
          actionListener="#{overviewController.click}" label="#{overviewBundle.toolbar_dropDownAction}" >
          <f:facet name="menupopup">
            <t:menu>
              <t:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
              <t:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
              <t:menuItem id="dropdown3_2" actionListener="#{overviewController.click}" label="Action 3"/>
            </t:menu>
          </f:facet>
        </t:toolBarCommand>

        </t:toolBar>
        </t:cell>

        <t:in value="#{overviewController.lastAction}" readonly="true"
            label="#{overviewBundle.basic_lastActionLabel}" />

        <t:selectOneChoice value="#{demo.toolbarTextPosition}" >
          <f:selectItems value="#{demo.toolbarTextItems}"/>
        </t:selectOneChoice>
        <t:selectOneChoice value="#{demo.toolbarIconSize}" >
          <f:selectItems value="#{demo.toolbarIconItems}"/>
        </t:selectOneChoice>

        <t:cell/>
        <t:cell spanX="2">
          <t:button label="updateView"  />
        </t:cell>

      </t:box>

      <t:cell/>

      <t:out escape="false" value="#{overviewBundle.toolbar_text2}" />

      <t:box label="#{overviewBundle.toolbar_sampleTitle2}" id="boxMenu">
        <t:menuBar >

          <t:menu label="Menu 1" >

            <t:menuItem type="script" action="alert('action 1')"  label="alert 1" />

            <t:menuItem action="/" type="navigate" label="#{bundle.linkClickme}"
                image="image/config.gif" >
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
      </t:box>
      <t:cell/>
    </t:panel>
  </jsp:body>
</layout:overview>
