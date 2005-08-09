%><%@ page errorPage="/errorPage.jsp"
%><%@ page contentType="text/vnd.wap.wml"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t"
%><%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"
%><?xml version='1.0'?>
<!DOCTYPE wml PUBLIC '-//WAPFORUM//DTD WML 1.1//EN'
 'http://www.wapforum.org/DTD/wml_1.1.xml'>

<wml>
  <card id="Content" title="Willkommen in meiner Galaxie">
    <p>Merkur,
      <a href="#venus">Venus</a>, Erde, Mars,

      <a href="testwml2.jsp">Pops</a>
    </p>
  </card>
  <card id="venus" title="Venus">
    <p> Ein Tag auf der Venus entspricht 117 Erdentagen. Ein Venusjahr dauert
    225 Tage. Die durchschnittliche Oberflaechentemperatur der Venus betraegt
    460 Grad Celsius. Der Oberflaechendruck betraegt 95 Atmosphaeren. Das
    haeufigste Atmosphaerengas ist Kohlendioxid.</p>
    <p align="center">
      <t:in value="#{name}" id="name"
        label="Name:" />
      <br/>
      <t:in value="#{alter}" id="alter"
        label="Alter:" />
      <br/>
      <t:in value="#{sternzeichen}" id="sternzeichen"
        label="Sternzeichen:" />
    </p>
    <p align="center">
      <select>
        <option onpick="#Content">Home</option>
        <option onpick="#venus">venus</option>
        <option onpick="testwml2.jsp">Pops</option>
      </select>
    </p>
  </card>
</wml>
