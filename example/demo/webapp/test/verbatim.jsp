<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <t:page label="testing: verbatim">

    <f:verbatim>
      <b>Hallo</b>
    </f:verbatim>

    <f:verbatim escape="true" > fixme: escape doesn't work
      <i>Hallo</i>
    </f:verbatim>

    <hr><!-- this uses also VerbatimTag -->


    <t:panel>

         <f:verbatim>
           <b>Hallo</b>
         </f:verbatim>

         <f:verbatim escape="true" > fixme: escape doesn't work
           <i>Hallo</i>
         </f:verbatim>


    </t:panel>

    <hr><!-- this uses also VerbatimTag -->

    <t:button type="button">
      verbatim button text
    </t:button>

    <hr><!-- this uses also VerbatimTag -->

    <t:button type="button">
      <f:verbatim>
        verbatim button text
      </f:verbatim>
    </t:button>

    <hr><!-- this uses also VerbatimTag -->

    <t:button type="button">
      <f:verbatim>
        verbatim button text
      </f:verbatim>
    </t:button>

  </t:page>
</f:view>
