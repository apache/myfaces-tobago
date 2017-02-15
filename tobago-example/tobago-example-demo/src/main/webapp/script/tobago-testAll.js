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

Tobago.registerListener(function() {
  jQuery("iframe").each(function() {
    jQuery(this).load(function() {

      var idCount = jQuery(this).attr("id").substring(7);
      var $thisFrame = jQuery("#page\\:tp" + idCount);
      var $nextFrame = jQuery("#page\\:tp" + ++idCount);

      waitForTest(function() {
        $thisFrame = jQuery($thisFrame.selector);
        return $thisFrame.contents().find("#qunit-banner").length > 0
            && $thisFrame.contents().find("#qunit-banner").attr("class") != "";
      }, function() {
        $nextFrame = jQuery($nextFrame.selector);
        runNextFrame($nextFrame);
      });
    });
  });

  var $firstFrame = jQuery("#page\\:tp1");
  runNextFrame($firstFrame);
}, Tobago.Phase.DOCUMENT_READY);

function waitForTest(waitingDone, executeWhenDone) {
  var stillWaiting = true;
  var interval = setInterval(function() {
    if (stillWaiting) {
      stillWaiting = !waitingDone();
    } else {
      executeWhenDone();
      clearInterval(interval);
    }
  }, 500);
}

function runNextFrame($nextFrame) {
  if ($nextFrame.length > 0) {
    var url = $nextFrame.attr("name");
    if (url != undefined) {
      $nextFrame.removeAttr("name");
      $nextFrame.attr("src", url);
    }
  }
}
