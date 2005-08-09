<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%>

<t:box label="Inhalt" id="panel">
  <f:facet name="layout">
    <t:gridLayout columns="1*;1*" border="1" id="layout" />
  </f:facet>

    <t:cell spanX="2">
      <t:link action="#" type="navigate"  label="Einleitung" />
    </t:cell>

    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_1"/>
    <t:link action="#" type="navigate"  label="Was ist Tobago" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_2" />
    <t:link action="#" type="navigate"  label="Für wen ist Tobago" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_3" />
    <t:link action="#" type="navigate"  label="Vorteile" />

    <%-- Tobago Spacer Cell--%>
    <t:cell spanX="2">
      <t:image value="image/blind.gif" width="1" height="10" id="image_4" />
    </t:cell>
    <%-- Tobago Spacer Cell--%>

    <t:cell spanX="2">
      <t:link id="Vorteile" action="#" type="navigate"  label="Installation" />
    </t:cell>
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_5" /> <%-- fixme: link has no bodycontent --%>
    <t:link id="Systemvorraussetzungen" action="#" type="navigate"  label="Systemvorraussetzungen" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_6" />
    <t:link id="Software" action="#" type="navigate"  label="Software" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_7" />
    <t:link id="Installation" action="#" type="navigate"  label="Installation" />

    <%-- Tobago Spacer Cell--%>
    <t:cell spanX="2">
      <t:image value="image/blind.gif" width="1" height="10" id="image_8" />
    </t:cell>
    <%-- Tobago Spacer Cell--%>

    <t:cell spanX="2">
      <t:link id="Features" action="#" type="navigate"  label="Features" />
    </t:cell>
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_9" />
    <t:link id="Trennung" action="#" type="navigate"  label="Trennung von Bussinesslogik und Design" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_10" />
    <t:link id="i18n" action="#" type="navigate"  label="Internationalisierung" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_11" />
    <t:link id="Kompatibiltaet" action="#" type="navigate"  label="Ausgabe Kompatibiltät" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_12" />
    <t:link id="Systemunabhaengikeit" action="#" type="navigate"  label="Systemunabhängikeit" />

    <%-- Tobago Spacer Cell--%>
    <t:cell spanX="2">
      <t:image value="image/blind.gif" width="1" height="10" id="image_13" />
    </t:cell>
    <%-- Tobago Spacer Cell--%>

    <t:cell spanX="2">
      <t:link id="Tags" action="#" type="navigate"  label="Tobago Tags" />
    </t:cell>
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_14" />
    <t:link id="Beispielcode" action="#" type="navigate"  label="Beispielcode" />

    <%-- Tobago Spacer Cell--%>
    <t:cell spanX="2">
      <t:image value="image/blind.gif" width="1" height="10" id="image_15" />
    </t:cell>
    <%-- Tobago Spacer Cell--%>

    <t:cell spanX="2">
      <t:link id="Beispiel" action="#" type="navigate"  label="Beispiel Applikation" />
    </t:cell>
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_16" />
    <t:link id="Banking" action="#" type="navigate" label="Home Banking" />
    <t:image value="image/blind.gif" alt="" width="8" border="0" id="image_17" />
    <t:link id="Portal" action="#" type="navigate" label="Nachrichten Portal" />
    <t:image value="image/blind.gif" alt="" width="8" border="0"  id="image_18"/>
    <t:link id="Home" action="#" type="navigate" label="Home" />

</t:box>
