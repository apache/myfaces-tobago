/* Copyright 2002-2005 atanion GmbH.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

function openPopup(url,name,width,height,x,y) {
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
    var url = 'http://www.atanion.net';
  }
  var options = "width=" + width + "," + "height=" + height;


//  alert("options" + options)
//  document.write("isMac: " + isMac + "<br />");
//  document.write("navigator.appVersion: " + navigator.appVersion + "<br />");
//  document.write("navigator.appName: " + navigator.appName + "<br />");
//  document.write("navigator.platform: " + navigator.platform + "<br />");
//  document.write("userAgent: " + navigator.userAgent.toLowerCase() + "<br />");
//  var ua = navigator.userAgent.toLowerCase();
//  document.write(ua);
//  if (ua.indexOf('firefox'))  {
//    document.write("userAgent is index of firefox" + "<br />");
//  } else  {
//    document.write("userAgent is NOT index of firefox" + "<br />");
//  }
  window.open(url,name,options);
}
