<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <t:page label="Address Editor" id="page" width="640px" height="480px">

    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:box label="Address Editor" >
      <f:facet name="layout">
        <t:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*;fixed"  />
      </f:facet>

      <t:messages />

      <t:in value="controller.currentAddress.firstName"
          label="First Name" />

      <t:in value="controller.currentAddress.lastName"
          label="Last Name" rendered="true" />

      <t:panel>
        <f:facet name="layout">
          <t:gridLayout columns="6*;1*" />
        </f:facet>
        <t:in value="controller.currentAddress.street"
            label="Street" />
        <t:in value="controller.currentAddress.houseNumber" />
      </t:panel>

      <t:panel>
        <f:facet name="layout">
          <t:gridLayout columns="1*;1*" />
        </f:facet>
        <t:in value="controller.currentAddress.zipCode"
            label="City" />
        <t:in value="controller.currentAddress.city" />
      </t:panel>

      <t:in value="controller.currentAddress.country"
          label="Country" />

      <t:in value="controller.currentAddress.phone"
          label="Phone" />

      <t:in value="controller.currentAddress.mobile"
          label="Mobile" />

      <t:in value="controller.currentAddress.fax"
          label="Fax" />

      <t:in value="controller.currentAddress.email"
          label="Email" />

      <t:date value="controller.currentAddress.dayOfBirth"
          label="Day of Birth">
        <f:convertDateTime pattern="dd.MM.yyyy" />
      </t:date>

      <t:textarea value="controller.currentAddress.note"
          label="Note" />

      <t:panel>
        <f:facet name="layout">
          <t:gridLayout columns="3*;1*;1*"  />
        </f:facet>

        <t:cell />
        <t:button action="controller.storeAddress"
            label="Store" defaultCommand="true" />
        <t:button action="list" immediate="true"
            labelWithAccessKey="_Cancel" />
      </t:panel>

    </t:box>
  </t:page>
</f:view>
