<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="menu">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" />
        </f:facet>

          <t:menuBar>
            <t:menu labelWithAccessKey="_File">
              <t:menuItem label="New File" />
              <t:menuItem label="Open File" />
              <t:menuItem label="Save" />
              <t:menuItem label="Print" />
              <t:menuSeparator />
              <t:menuItem label="Exit" />
              <t:menucheck label="Administration Mode"/>
            </t:menu>

            <t:menu labelWithAccessKey="_Edit">
              <t:menuItem label="Copy" />
              <t:menuItem label="Cut" />
              <t:menuItem label="Paste" />
              <t:menuItem label="Delete" />
              <t:menuSeparator />
                <t:menu labelWithAccessKey="_Delete">
                  <t:menuItem label="As Spam" />
                  <t:menuItem label="As Newsletter" />
                  <t:menuItem label="As Uninteresting" />
                </t:menu>
            </t:menu>

            <t:menu labelWithAccessKey="_View">
              <t:menuItem label="Snap to Grid" />
              <t:menuItem label="Show Rulers" />
            </t:menu>
          </t:menuBar>

        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>