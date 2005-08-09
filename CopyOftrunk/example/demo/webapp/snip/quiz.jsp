<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="quiz_jsp" >

  <t:box label="Quiz">
    <b>Unser Sonnensystem</b> (Lückentext)
    <p>
      In der
      <t:in value="#{demo.planet[0]}" width="100px" inline="true" />
      unseres Sonnensystems steht die
      <t:in value="#{demo.planet[1]}" width="100px" inline="true" />
      . Sie spendet uns Licht uns
      <t:in value="#{demo.planet[2]}" width="100px" inline="true" />
      . Ohne sie wäre kein Leben auf der
      <t:in value="#{demo.planet[3]}" width="100px" inline="true" />
       möglich. Die Sonne ist ein
      <t:in value="#{demo.planet[4]}" width="100px" inline="true" />
      . Alle
      <t:in value="#{demo.planet[5]}" width="100px" inline="true" />
       Planeten unseres Sonnensystems kreisen
      in einer ellipsen
      <t:in value="#{demo.planet[6]}" width="100px" inline="true" />
      Bahn um die Sonne.
      <t:in value="#{demo.planet[7]}" width="100px" inline="true" />
      ist am nähesten zur Sonne. Seine Oberfläche ist mit
      <t:in value="#{demo.planet[8]}" width="100px" inline="true" />
      übersät. Er ist zwar
      <t:in value="#{demo.planet[9]}" width="100px" inline="true" />
      als der
      <t:in value="#{demo.planet[10]}" width="100px" inline="true" />
      , aber nur
      <t:in value="#{demo.planet[11]}" width="100px" inline="true" />
      so gro&szlig; wie die
      <t:in value="#{demo.planet[12]}" width="100px" inline="true" />
      . Die
      <t:in value="#{demo.planet[13]}" width="100px" inline="true" />
      ist so hell, dass man sie als ersten Stern am
      <t:in value="#{demo.planet[14]}" width="100px" inline="true" />
      sehen kann. Sie wird auch
      <t:in value="#{demo.planet[15]}" width="100px" inline="true" />
      ­ oder Abendstern genannt. Ihre
      <t:in value="#{demo.planet[16]}" width="100px" inline="true" />
      ist von dicken wei&szlig;en Wolken umhüllt. Die Venus ist der
      <t:in value="#{demo.planet[17]}" width="100px" inline="true" />
      Planet unseres Sonnensystems. Die Erde wird auch der &ldquo;
      <t:in value="#{demo.planet[18]}" width="100px" inline="true" />
      Planet&rdquo; genannt. Sie ist der
      einzige Planet des Sonnensystems, auf dem es W
      <t:in value="#{demo.planet[19]}" width="100px" inline="true" />
      und L
      <t:in value="#{demo.planet[20]}" width="100px" inline="true" />
      gibt. Sie braucht für ihre Bahn um die
      <t:in value="#{demo.planet[21]}" width="100px" inline="true" />
      365 Tage und 6 Stunden
      -- ein
      <t:in value="#{demo.planet[22]}" width="100px" inline="true" />
      . Der
      <t:in value="#{demo.planet[23]}" width="100px" inline="true" />
      besteht aus
      <t:in value="#{demo.planet[24]}" width="100px" inline="true" />
      G <t:in value="#{demo.planet[25]}" width="100px" inline="true" />
      . Früher dachte man, auf ihm lebten

      <t:in value="#{demo.planet[26]}" width="100px" inline="true" />
      , die sogenannten
      &ldquo;Mars­Menschen&rdquo;. Der Jupiter ist der
      <t:in value="#{demo.planet[27]}" width="100px" inline="true" />
      der neun Planeten, aber er besteht nur
      aus
      <t:in value="#{demo.planet[28]}" width="100px" inline="true" />
      und Flüssigkeiten.
      Er hat keine
      <t:in value="#{demo.planet[29]}" width="100px" inline="true" />
      <t:in value="#{demo.planet[30]}" width="100px" inline="true" />
      . Auch der
      <t:in value="#{demo.planet[31]}" width="100px" inline="true" />
      ist ein
      &ldquo;Gasriese&rdquo; wie der Jupiter. Auch wegen seiner
      <t:in value="#{demo.planet[32]}" width="100px" inline="true" />
      , die aus E
      <t:in value="#{demo.planet[33]}" width="100px" inline="true" />
      und St
      <t:in value="#{demo.planet[34]}" width="100px" inline="true" />
      bestehen, ist er sehr bekannt. Der
      Saturn hat zehn
      <t:in value="#{demo.planet[35]}" width="100px" inline="true" />
      , von
      denen einer fast so gro&szlig; wie der Merkur ist. Uranus -- der erste
      Planet, den man mit Hilfe eines T
      <t:in value="#{demo.planet[36]}" width="100px" inline="true" />
       entdeckte -- besteht zur Gänze aus gefrorenen Gasen. Neptun ist der
      entfernteste Planet, den man mit einer Raumsonde
      <t:in value="#{demo.planet[37]}" width="100px" inline="true" />
       konnte. Pluto ist
      <t:in value="#{demo.planet[38]}" width="100px" inline="true" />
       von der Sonne entfernt und der kleinste
      der Planeten. Er hat einen Mond, den man Charon nennt. Weil Pluto und
      Charon fast gleich gro&szlig; sind, bezeichnet man die beiden zusammen
      auch als &ldquo;
      <t:in value="#{demo.planet[39]}" width="100px" inline="true" />
      &rdquo;. Unser
      Sonnensystem befindet sich zusammen mit vielen anderen Sonnensystemen in
      der Galaxie, die man &ldquo;
      <t:in value="#{demo.planet[40]}" width="100px" inline="true" />
      &rdquo; nennt.
    </p>
  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button label="#{bundle.submit}" />

    <t:cell />

  </t:panel>

</f:subview>
