/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


function openPopup(url,name,width,height,options,x,y) {
  //Defaults
  if (!name)  {
    var name= "name";
  }
  if (!width) {
    var width = 800;
  }
  if (!height)  {
    var height = 600;
  }
  if (!x) {
    var x = parseInt((window.screen.availWidth - width)/2);
  }
  if (!y) {
    var y = parseInt((window.screen.availHeight - height)/2);
  }
  if (!url) {
    var url = '';
  }
  if (!options) {
    para = "";
  }

  var para = setPopupPara(width,height,options);

  var newwin = window.open(url,name,para);

  if (window.focus) {
    newwin.focus()
  }
}

function setPopupPara(width,height,options) {

	var parent = "";
	var dirbar = "";
	var locationbar = "";
	var menubar = "";
	var resizable = "";
	var scrollbars = "";
	var statusbar = "";
	var toolbar = "";
	if (options) {
    if (options.indexOf("p") > -1 ) { parent = ",dependent"	}
		if (options.indexOf("d") > -1 ) { dirbar = ",directories"	}
		if (options.indexOf("l") > -1 ) { locationbar = ",location"	}
		if (options.indexOf("m") > -1 ) { menubar = ",menubar"	}
		if (options.indexOf("r") > -1 ) { resizable = ",resizable"	}
		if (options.indexOf("s") > -1 ) { scrollbars = ",scrollbars" }
		if (options.indexOf("u") > -1 ) { statusbar = ",status"	}
		if (options.indexOf("t") > -1 ) { toolbar = ",toolbar"	}
	}
  var width=",width = " + width;
  var height=",height = " + height;
  return width + height + parent + dirbar + locationbar
      + menubar + resizable + scrollbars + statusbar + toolbar;
}
