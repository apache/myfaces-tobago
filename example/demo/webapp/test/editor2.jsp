       <%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
       <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
       <f:view>
         <t:page label="Address Editor" id="page" width="640px" height="480px">


           <t:box label="Address Editor" >

             <t:in value="#{controller.currentAddress.firstName}"
                 label="First Name" />

             <t:in value="#{controller.currentAddress.lastName}"
                 label="Last Name" rendered="true" />

             <t:panel>
               <t:in value="#{controller.currentAddress.street}"
                   label="Street" />
               <t:in value="#{controller.currentAddress.houseNumber}" />
             </t:panel>

             <t:panel>

               <t:in value="#{controller.currentAddress.zipCode}"
                   label="City" />
               <t:in value="#{controller.currentAddress.city}" />
             </t:panel>

             <t:in value="#{controller.currentAddress.country}"
                 label="Country" />

             <t:in value="#{controller.currentAddress.phone}"
                 label="Phone" />

             <t:in value="#{controller.currentAddress.mobile}"
                 label="Mobile" />

             <t:in value="#{controller.currentAddress.fax}"
                 label="Fax" />

             <t:in value="#{controller.currentAddress.email}"
                 label="Email" />


             <t:textarea value="#{controller.currentAddress.note}"
                 label="Note" />


           </t:box>
         </t:page>
       </f:view>
