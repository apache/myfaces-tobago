// Beispiel Aufruf: openPopup('http://www.atanion.com','popup',700,450,'')

var ie = 0;
var agent = navigator.userAgent.toLowerCase();
var isMac = (agent.indexOf('mac') > -1) ? true : false;

if (navigator.appName.indexOf("Explorer") > -1) {
	if (navigator.appVersion.indexOf("MSIE 5") > -1) { ie = 5 }
	else {
		if (navigator.appVersion.indexOf("MSIE 3") > -1) { ie = 3 }
		else { ie = parseInt(navigator.appVersion.substring(22,23)) ?parseInt(navigator.appVersion.substring(22,23)) : 0 }
	}
}

// Liste geoeffneter Popups
var openedPopups = new Array();

// Default-Werte best. Parameter
var defaults = new Array();
defaults['url'] = ""; // Default-URL: leeres fenster (was z.B. mit Javascripts fenster.document.write() gefuellt werden kann
defaults['name'] = "popup"; // Default-Name des Popups
defaults['width'] = 600; // Default-Breite
defaults['height'] = 400; // Default-Hoehe

function openPopup(url,name,w,h,switches,x,y) {

	// ++ URL setzen ++
	if (!url) { url = defaults['url'] }


	// ++ Fenstername setzen ++
	if (!name) { name = defaults['name'] }


	// Breite nachbearbeiten
	if (!w) { w = defaults['width'] }
	else { w = parseInt(w) } // in Integer umwandeln
//	if (isMac && ie){ w -= 20 } // auf dem Mac zu gross
	if (isMac && ie && ie < 5){ w -= 20 } // auf dem Mac zu gross


	// Hoehe nachbearbeiten
	if (!h) { h = defaults['height'] }
	else { h = parseInt(h) } // in Integer umwandeln
	if (ie && ie < 5) { h -= 20} // der Explorer 4- macht das Fenster zu gross



	// Breite und Hoehe nachbearbeiten wenn Vollbildmodus
	if (switches && switches.indexOf("f") > -1 && window.screen) { // wenn Schalter 'f' und screen-Objekt vorhanden
		/*w = "outerWidth=" + window.screen.availWidth;
		h = ",outerHeight=" + window.screen.availHeight; */
		w = window.screen.availWidth;
		h = window.screen.availHeight;
	}


	// Position berechnen
	var xpos; // keine Defaultposition
	var ypos;
	var xInt = parseInt(x); // vermeidet Fehlermeldungen im IE3 wenn x und y nicht gesetzt
	var yInt = parseInt(y);
	if (xInt > -1 && yInt > -1) { // an best. Position x/y (Koordinaten linker oberer Fensterecke)
		xpos = xInt;
		ypos = yInt;
	}
	else {
		if (switches && window.screen) { // wenn Schalter und screen-Objekt vorhanden
			if (switches.indexOf("c") > -1) { // wenn Schalter 'c' = centered
				// Koordinaten fuer bildschirmmittige Positionierung errechnen
				xpos = parseInt((window.screen.availWidth - w)/2);
				ypos = parseInt((window.screen.availHeight - h)/2);
			}
			if (switches.indexOf("f") > -1) { // wenn Schalter 'f' = fullscreen
				xpos = 0;
				ypos = 0;
			}
		}
	}


	// ++ Parameterliste setzen ++ //
	var params = getPopupParams(w,h,switches,xpos,ypos);


	// Automatisches Schliessen aller offenen Popups wenn erwuenscht. Nicht in IE3 wg. Fehlermeldungen.
	if (switches && switches.indexOf('a') > -1 && window.close){
		closePopups(); // alle offenen Popups schliessen
	}

	// zuerst ggf. noch offenes Popup gleichen Namens schliessen
/*	if (openedPopups[name] && openedPopups[name].close && !openedPopups[name].closed) { // wenn Eintrag und close-Funktion vorhanden und noch offen
		openedPopups[name].close(); // Popup schliessen
		openedPopups[name] = ""; // Referenz loeschen
	}
*/

	// ++ jetzt geht's los: Popup oeffnen ++

	// neues Fenster oeffnen
	var newwin = window.open(url, name, params);

	// ++ Nachbereitungen ++
	// Wenn der Schalter 'b' (fuer rahmenlose Fenster) angegeben wurde, wurde das Fenster in
	// fullscreen geoeffnet. Jetzt muss anschliessend seine Groesse und Position geaendert werden.
	if (switches && switches.indexOf('b') > -1 && window.moveTo && window.resizeTo && window.focus) { // wenn Schalter 'b' (borderless) sowie Methoden moveTo, resizeTo und focus vorhanden
		// Popup waehrend Groessenaenderung "unsichtbar" machen
		newwin.blur();	// Fenster verlassen
		newwin.opener.focus(); // Herkunftsfenster nach vorne bringen
		// Popup von Fullscreen auf kleinere Groesse setzen
		newwin.resizeTo(w,h)
		newwin.moveTo(xpos,ypos)
	}

	 // wenn focus unterstuetzt wird: Popup nach vorne bringen
	if (window.focus) { newwin.focus() }

	// Popup-Referenz speichern. Wert kann mit getWindow(fenstername) abgefragt werden.
	openedPopups[name] = newwin;

}

// Funktion fuer das Zusammenbasteln der Popup-Parameter
function getPopupParams (w,h,switches,xpos,ypos) {

	// Breite setzen
	var width = "width=" + w;

	// Hoehe setzen
	var height = ",height=" + h;

	// weitere Parameter setzen
	var parent = ""; // z.Zt. nur NN4+
	var dirbar = "";
	var fullscreen = ""; // z.Zt. nur IE3+
	var hotkeys = ""; // z.Zt. nur NN4+
	var locbar = "";
	var menubar = "";
	var resizable = "";
	var scrollbars = "";
	var statusbar = "";
	var toolbar = "";
	if (switches) {
		if (switches.indexOf("p") > -1 ) { parent = ",dependent"	}
		if (switches.indexOf("d") > -1 ) { dirbar = ",directories"	}
		if (switches.indexOf("h") > -1 ) { hotkeys = ",hotkeys=no"	}
		if (switches.indexOf("l") > -1 ) { locbar = ",location"	}
		if (switches.indexOf("m") > -1 ) { menubar = ",menubar"	}
		if (switches.indexOf("r") > -1 ) { resizable = ",resizable"	}
		if (switches.indexOf("s") > -1 ) { scrollbars = ",scrollbars" }
		if (switches.indexOf("u") > -1 ) { statusbar = ",status"	}
		if (switches.indexOf("t") > -1 ) { toolbar = ",toolbar"	}
		if (switches.indexOf("k") > -1 || switches.indexOf("b") > -1 ) { fullscreen = ",fullscreen=1" }
	}

	// Popup-Position (x/y-Koordinate) setzen
	var pos = "";
	var xposInt = parseInt(xpos); // vermeidet Fehlermeldungen im IE3 wenn xpos und ypos nicht gesetzt
	var yposInt = parseInt(ypos);
	if (xposInt > -1 && yposInt > -1) { pos = ",left=" + xposInt + ",top=" + yposInt }

	// Parameterliste zusammensetzen und zureuckgeben
	return width + height + parent + dirbar + hotkeys + locbar + menubar + resizable + scrollbars + statusbar + toolbar + pos + fullscreen;
}
