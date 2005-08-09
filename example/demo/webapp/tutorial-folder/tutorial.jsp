<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: tutorial.jsp 1178 2005-02-22 11:29:05 +0100 (Di, 22 Feb 2005) lofwyr $
  --%><%@ page errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%
  request.setAttribute("exampleString", "sample");
%>
<f:subview id="tutorial_jsp" >

     <table width="600" border="1" cellspacing="0" cellpadding="0" summary="">
      <tr>
        <td><jsp:include page="navigation.jsp" /></td>
      </tr>

      <tr>
        <td colspan="4">

        <t:box label="Einleitung">
          <t:out escape="false" value="<b>Einleitung</b>"/>
          <t:out value="Was ist Tobago?"/>
          <t:out value="Tobago ist eine von der Atanion GmbH entwickelte
          JSP Tag Library zum entwickeln von GUI´s für Webapplikationen."/>
          <br>
          <t:out value="<b>Für wen ist Tobago?</b>"/>
          <t:out value="Tobago ist sowohl für den ambitionierten Software
          Entwickler, der sich nicht mit HTML, CSS, JavaScript und den damit verbundenen
          Implementierungschwierigkeiten in verschiedenen Browsern beschäftigen möchte,
          als auch für den Designer, der nicht zu tief in die Welten der Java
          Programmierung abtauchen möchte."/>
          <br>
          <t:out value="<b>Vorteile?</b>"/>
          <t:out value="Tobago ermöglicht die strikte Trennung von Design
          und Businesslogik so das &Auml;nderungen am Design oder an der Businesslogik
          den jeweils anderen Part nicht affektieren. Desweiteren braucht man sich
          mit Tobago keine Gedanken um das Rendering der Komponenten machen, da
          die je nach Implementierung automatisch gerendert, formatiert und
          gelayoutet werden." />
          <br>
          <t:out value="<b>Systemvorraussetzungen</b>" />
          <t:out value="Software: JDK 1.4.1, beliebiger JSP Container
          (Tomcat 4.1, Weblogic, WebSphere), Jakarta Ant ab 1.4 zum deployen" />
          <br>
          <t:out value="<b>Installation</b>" />
          <t:out value="*****" />
          </t:box>
          <t:box>
          <br>
          <t:out value="Tobago bietet folgende Features (Auswahl):" /><br>

          <ul>
          <li><t:out value="Automatisches Rendern der GUI" /></li>
          <li><t:out value="Theme Unterstützung" /></li>
          <li><t:out value="Internationalisierung" /></li>
          <li><t:out value="vorgefertigte UI Komponenten wie: ColorChooser,
          TreeControl, TabControl, ProgressControl..." /></li>
          </ul>
          <br>
          <t:out value="<b>Trennung von Bussinesslogik und Design</b>" />
          <t:out value="Eine der Kernkompetenzen von Tobago ist es die
          Businesslogik einer Applikation frei von designtechnischen Einflüssen
          zu halten. So ist es zum Beispiel möglich ein und derselben Applikation
          verschiedene Designs zu geben (z.B. um Sie an ein bestehendes Design anzupassen),
          ohne etwas im Anwendungs-Sourcecode ändern zu müssen" />
          <br>
          <t:out value="<b>Internationalisierung</b>" />
          <t:out  value="Tobago unterstützt die Internationaliesierung mit
          i18n. Die &Uuml;bersezungen werden aus sog. property Dateien geladen und
          die Inhalte werden anhand der Variablen identifiziert.<br><br>
          nav_introduction=Einleitung<br>
          nav_introduction=Introduction<br>
          Diese beiden Zuweisungen würden in einer deutschen und in einer englischen
          property Datei stehen, womit dann je nach Einstellung des Users die
          richtige Sprache gewählt werden würde. Natürlich sind hier auch
          Fallback Mechanismen implementiert falls ein Key nicht vorhanden ist." />
          <br>
          <t:out value="<b>Unabhängikkeit vom UserAgent</b>" />
          <t:out value="Tobago gibt Ihnen die Freiheit sich nicht um die
          verschiedenen Ausgabe Formate (html, wml, imode...) kümmern zu müssen.
          Inhalte werden, abhängig davon welcher UserAgent die Seite anfordert,
          für das richtige Endgerät gerendert und ausgegeben. " />
          <br>
          </t:box>

          <t:box label="Tobago Tags">
          <t:out value="<b>Tobago Tag Beispiele</b>" />
          <t:out value="Die Tags sind entsprechend üblicher JSP Tag
          schreibweise aufgebaut:"  />
          <br>
          <pre>
          &lt;tobago:link url="helloworld.jsp"&gt;
          &lt;tobago:text value="Hier steht der Link Text" /&gt;
          &lt;/tobago:link&gt;
          </pre>

          <t:out value="Jeder Tobago Tag beginnt also mit dem JSP
          typischen Aufbau: &quot;tobago:&quot; Was folgt ist der Name des zu
          rendernden Objekts. In diesem Fall wird ein Link für den passenden
          User Agent (in html, wml...) erzeugt. Als Attribute können hier
          verschiedene mitgegeben werden. Sinnvoll ist es hier dem Link eine URL
          mitzugeben. Hier &quot;helloworld.jsp&quot;. Um dem User auch ein
          anklicken des Links zu ermöglichen, wird ein &quot;tobago:text&quot;
          Element von dem Link umschlossen." />
          <br>
          <t:out value="Dies ist der von der Tag Library je nach Theme
          erzeugte Output:" />
          <br>
          <t:link action="helloworld.jsp" type="navigate" label="Hier steht der Link Text" />
          <br>
          <t:out value=" und hier der erzeugte html Quellcode:" />
          <pre>&lt;a class="link-block" href="helloworld.jsp"&gt;&lt;span&gt;Hier
          steht der Link Text&lt;/span&gt;&lt;/a&gt;</pre>
          <br>

          <t:out value="<b>Standard Attribute</b>" />
          <t:out value="Jedes Tobago Tag besitzt einige Standard Attribute
          zum ändern der Default Eigenschaften:" />
          <br>
          <t:out value="inline=&quot;true | false" />
          <t:out value="Alle Elemente in Tobago werden per Default als
          Blockelemente gerendert. Das heisst, nach jedem Element beginnt ein
          neuer Absatz. Um dies zu verhindern steht jedem Tobago Tag das Attribut
          &quot;inline&quot; zur Verfügung, das mit &quot;true&quot; oder &quot;
          false&quot; an- und ausgeschaltet werden kann. Falls das Attribut auf
          &quot;false&quot; steht wird das Element in eine Zeile mit den
          Umgebenen Controls gesetzt." />
          <br>

          <t:out value="<b>Beispiel für inline=&quot;true | false&quot;</b>" />
          <br>
          <t:out value="<b>non-inline</b>" />
          <t:out value="Dieses ist der erste Absatz" />
          <t:out value="Und dies ist der zweite Absatz" />
          <br>
          <t:out value="<b>inline</b>" />
          <t:out value="Dieser Text ist ein inline Element." inline="true" />
          <t:out value="Dieses ist der zweite inline Text." inline="true" />

          <br>
          <br>
          <br>
          <t:out value="<b>id=&quot;value&quot;</b>" />
          <t:out value="Jedem Element kann eine ID zugeordnet werden um
          z.B. per HTML oder JavaScript auf dieses Element zuzugreifen oder es
          per CSS zu formatieren." />
          <br>
          <t:out value="Beispiel für ein HTML Label, das mit Hilfe der ID
          auf das &quot;Text Input&quot; referenziert. Man kann nun auf den zu dem &quot;Input&quot;
          gehörenden Text klicken und gelangt in das zugehörige &quot;Input&quot;" />
          <br>
          <t:in value="#{exampleString}" label="Name:" />

          <t:in value="#{exampleString}" label="Vorname:" />
          <br>
          <br>
          <t:out value="<b>disabled=&quot;true | false&quot;</b>" />
          <t:out value="Bei vielen Tags kann man die Anzeige auf
          disabled=&quot;true&quot; setzen. Dieser Tag (z.B. Input Felder oder
          Buttons) erscheinen dann inaktiv und können nicht angeklickt oder
          ausgefüllt werden." />
          <br>
          <t:in value="#{exampleString}" disabled="true" label="Vorname:" />
          <br>
          <br>
          <t:out value="<b>i18n</b>" />
          <t:out value="Bei jedem Control, bei dem es sinnvoll erscheint, kann
          die &Uuml;bersetzung eingeschaltet werden. Dies gilt insbesondere für
          normal Text, sowie auch für Box &Uuml;berschriften und Buttons.
          Layout gebende Controls bedürfen natürlich keiner &Uuml;bersetzung." />
          <br>
          <t:out value="&Uuml;bersetzter Text:" />
          <t:out value="#{bundle.nav_text}" />


          </t:box>

          <br>
      </td>
    </tr>
  </table>
</f:subview>
