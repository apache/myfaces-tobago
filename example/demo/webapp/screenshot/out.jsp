<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="out">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="300px;1*" />
        </f:facet>

          <t:out value="Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
          Quisque consequat, libero eget porta mattis, risus velit congue magna,
           at posuere sem orci vitae turpis. Integer pulvinar. Cras libero.
           Proin vestibulum tempor urna. Nulla odio nisl, auctor vitae, faucibus
           pharetra, feugiat eget, justo. Suspendisse at tellus non justo dictum
           tincidunt. Aenean placerat nunc id tortor. Donec mollis ornare pede.
           Vestibulum ut arcu et dolor auctor varius. Praesent tincidunt, eros
           quis vulputate facilisis, orci turpis sollicitudin justo, id faucibus
           nunc orci sed purus. Proin ligula erat, sollicitudin id, rhoncus
           eget, nonummy sit amet, risus. Aenean arcu lorem, facilisis et, posuere
           sed, ultrices tincidunt, nunc. Sed ac massa. Quisque lacinia. Donec
           quis nibh.

Aenean ac diam eget mi feugiat pulvinar. Etiam orci. Aliquam nec arcu nec eros
ornare pulvinar. Sed nec velit. Ut ut orci. Nulla varius. Maecenas feugiat.
Etiam varius ipsum et orci. Ut consectetuer odio sit amet libero. Nulla iaculis
adipiscing purus. Maecenas a sed." />
        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>