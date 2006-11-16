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

function calendarWindow(url) {

  //    var scrX = window.event.screenX;
  //    var scrY = window.event.screenY;
  var scrX = 300;
  var scrY = 300;

  var avaWidth = screen.availWidth;
  var avaHeight = screen.availHeight;

  //    alert(scrX+" "+ scrY+"\n"+avaWidth+" "+avaHeight );

  var pickerWidth = 275;
  var pickerHeight = 150;

  var xPos;
  var yPos;

  if (scrX + pickerWidth + 4 > avaWidth) {
    xPos = avaWidth - (pickerWidth + 4);
  } else {
    xPos = scrX;
  }
  if (scrY + pickerHeight + 24 > avaHeight) {
    yPos = avaHeight - (pickerHeight + 24);
  } else {
    yPos = scrY;
  }

  var calendarWindow = window.open(url, "Calendar",
      "width=" + pickerWidth +
      ",height=" + pickerHeight +
      ",left=" + xPos +
      ",top=" + yPos +
      ",scrollbars=0,resizable=0,resizable=0,dependent=1");
  calendarWindow.focus();

}
